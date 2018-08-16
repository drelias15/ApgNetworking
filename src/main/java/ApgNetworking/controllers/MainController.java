package ApgNetworking.controllers;

import ApgNetworking.configurations.CloudinaryConfig;
import ApgNetworking.models.*;
import ApgNetworking.repositories.*;
import ApgNetworking.services.UserService;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@Controller
public class MainController {
	@Autowired
	private UserService userService;
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserCourseRepository userCourseRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	CloudinaryConfig cloudc;
	@Autowired
	private PostRepository postRepository;


	@RequestMapping("/")
	public String LoadIndex() {
		return"index";
	}

	@RequestMapping("/login")
	public String Login() {
		return "login";
	}

	@GetMapping("/register")
	public String Registration(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("courses", courseRepository.findAll());
		return "userinfo";
	}

	@PostMapping("/processUser")
	public String ProcessUser(@Valid@ModelAttribute("user")User user,
							  BindingResult result, Model model,@RequestParam("file")MultipartFile file) {
		if (result.hasErrors()||file.isEmpty()) {
			model.addAttribute("courses", courseRepository.findAll());
			return "userinfo";
		} else {
			try {
				Map uploadResult =  cloudc.upload(file.getBytes(),
						ObjectUtils.asMap("resourcetype", "auto"));
				user.setPicUrl(uploadResult.get("url").toString());

				if(!user.getLinkedIn().startsWith("https://")){
					user.setLinkedIn("https://"+user.getLinkedIn());
				}
				if(!user.getGithub().startsWith("https://")){
					user.setGithub("https://"+user.getGithub());
				}

				userService.saveStudent(user);
			} catch (IOException e){
				e.printStackTrace();
				model.addAttribute("courses", courseRepository.findAll());
				return "redirect:/userinfo";
			}
			return "login";
		}
	}

	@RequestMapping ("/profile/{id}")
	public String GetProfile(@PathVariable("id") long id, Model model){
		model.addAttribute("user",
				userRepository.findById(id).get());
		return "profile";
	}

	//====================================================================
	// COURSES
	//====================================================================

	@GetMapping("/courseform")
	public String GetCourseForm(Model model) {
		model.addAttribute("course", new Course());
		return "courseform";
	}

	@PostMapping("/addcourse")
	public String AddCourse(@Valid @ModelAttribute("course") Course course,
					BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "courseform";
		} else {
			course.setActive(true);
			courseRepository.save(course);
			model.addAttribute("courses", courseRepository.findAll());
			return "courses";
		}
	}

	@RequestMapping("/courses")
	public String GetAllCourses(Model model){
		model.addAttribute("courses", courseRepository.findAll());
		return "courses";
	}

	@RequestMapping("/currentCourses")
	public String GetCurrentCourses(Model model) {
		model.addAttribute("courses", getCourses("current"));
		return "courses";
	}

	@RequestMapping("/pastCourses")
	public String GetPastCourses(Model model) {
		model.addAttribute("courses", getCourses("past"));
		return "courses";
	}

	private Collection<Course> getCourses(String semester){
		User user = userService.getCurrentUser();
		Role role = userService.getCurrentRole(user);

		// Role would only matter if a user had more than one role
		Collection<UserCourse> userCourses =
				userCourseRepository.findAllByUserAndRole(user, role);
		Iterator<UserCourse> userCourseIterator = userCourses.iterator();

		Collection<Course> courses = new ArrayList<>();

		while(userCourseIterator.hasNext()) {
			Course course = userCourseIterator.next().getCourse();
			String temp_semester = course.getSemester();
			if(temp_semester.equals(semester)) {
				courses.add(course);
			}
			userCourseIterator.remove();
		}

		return courses;
	}

	@RequestMapping ("/coursedetail/{id}")
	public String GetCourseDetail(@PathVariable("id") long id, Model model) {
		// Get course
		Course course = courseRepository.findById(id).get();
		Role instructorRole = roleRepository.findByRole("Instructor");
		Role studentRole = roleRepository.findByRole("Student");
		int postCount = 0;

		ArrayList<UserCourse> userCoursesInstructors =
				userCourseRepository.findAllByRoleAndCourse(instructorRole,
						course);
		Iterator<UserCourse> userCourseInstructorIterator =
				userCoursesInstructors.iterator();

		ArrayList<UserCourse> userCoursesStudents =
				userCourseRepository.findAllByRoleAndCourse(studentRole,
						course);
		Iterator<UserCourse> userCourseStudentIterator =
				userCoursesStudents.iterator();

		ArrayList<Post> allPosts = postRepository.findAllByCourse(course);
		Iterator<Post> postIterator = allPosts.iterator();

		ArrayList<User> instructors = new ArrayList<>();
		ArrayList<User> students = new ArrayList<>();
		ArrayList<Post> posts = new ArrayList<>();

		while(userCourseInstructorIterator.hasNext()){
			User user = userCourseInstructorIterator.next().getUser();
			instructors.add(user);
			userCourseInstructorIterator.remove();
		}

		while(userCourseStudentIterator.hasNext()){
			User user = userCourseStudentIterator.next().getUser();
			students.add(user);
			userCourseStudentIterator.remove();
		}

		while(postIterator.hasNext() && postCount < 5){
			posts.add(postIterator.next());
			postIterator.remove();
			postCount++;
		}

		model.addAttribute("post_count", allPosts.size());
		model.addAttribute("posts", posts);
		model.addAttribute("course", course);
		model.addAttribute("instructors", instructors);
		model.addAttribute("students", students);

		return "coursedetail";
	}

	@RequestMapping ("/disableCourse/{id}")
	public String DisableCourse(@PathVariable("id") long id, Model model){
		Course course = courseRepository.findById(id).get();
		course.setActive(false);
		courseRepository.save(course);
		return "redirect:/courses";
	}

	@RequestMapping ("/enableCourse/{id}")
	public String EnableCourse(@PathVariable("id") long id, Model model){
		Course course = courseRepository.findById(id).get();
		course.setActive(true);
		courseRepository.save(course);
		return "redirect:/courses";
	}

	//====================================================================
	// POSTS
	//====================================================================

	@RequestMapping ("/newpost/{id}")
	public String GetPostForm(@PathVariable("id") long id, Model model){
		model.addAttribute("course_id", id);
		model.addAttribute("post", new Post());
		return "postform";
	}

	@PostMapping("/postform")
	public String AddPost(@Valid @ModelAttribute("post") Post post,
						   BindingResult result, Model model, HttpServletRequest request){
		if (result.hasErrors()) {
			return "postform";
		}
		else {
			Course course =
					courseRepository.findById(new Long(request.getParameter(
							"course_id"))).get();
			post.setCourse(course);
			post.setUser(userService.getCurrentUser());
			if(!post.getLink().startsWith("https://")){
				post.setLink("https://"+post.getLink());
			}
			postRepository.save(post);
			model.addAttribute("course", course);
			model.addAttribute("posts", postRepository.findAllByCourse(course));
			return "posts";
		}
	}

	@RequestMapping("/posts/{id}")
	public String GetPosts(@PathVariable("id") long id, Model model){
		Course course = courseRepository.findById(id).get();
		model.addAttribute("course", course);
		model.addAttribute("posts", postRepository.findAllByCourse(course));
		return "posts";
	}
}
