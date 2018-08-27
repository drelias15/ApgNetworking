package ApgNetworking.controllers;

import ApgNetworking.configurations.CloudinaryConfig;
import ApgNetworking.models.*;
import ApgNetworking.repositories.*;
import ApgNetworking.services.NotificationService;
import ApgNetworking.services.UserService;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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
	@Autowired
	private PrivateMessageRepository privateMessageRepository;
	@Autowired
	private NotificationService notificationService;


	@RequestMapping("/")
	public String LoadIndex(Model model) {
        model.addAttribute("user", userService.getCurrentUser());

		return"index";
	}

	@RequestMapping("/login")
	public String Login() {
		return "login";
	}

	@RequestMapping("/exists")
	public String Exists() {
		return "usernameexists";
	}

	//====================================================================
	// USER
	//====================================================================

	@RequestMapping("/users")
	public String GetUsers(Model model) {
		model.addAttribute("users", userRepository.findAll());
        model.addAttribute("user", userService.getCurrentUser());
		return "admin/users";
	}

	@GetMapping("/register")
	public String Registration(Model model) {
		model.addAttribute("user", new User());
       // model.addAttribute("user", userService.getCurrentUser());
		return "userinfo";
	}

	@PostMapping("/register")
	public String AddUpdateUser(@Valid @ModelAttribute("user") User user,
							  BindingResult result, Model model,
							  @RequestParam("file") MultipartFile file,
							  @RequestParam("img_url") String img_url, Errors errors) {

		System.out.println("process form reached");

		if((file.isEmpty() && img_url.isEmpty()) || result.hasErrors()) {
			model.addAttribute("uploadPhotoMessage", "Profile photo is required.");
			model.addAttribute("img_url", img_url);
			return "userinfo";
		}

		try {
			// If (new) image selected, upload to Cloudinary
			if(!file.isEmpty()) {
				Map uploadResult = cloudc.upload(file.getBytes(),
						ObjectUtils.asMap("resourcetype", "auto"));
				user.setPicUrl(uploadResult.get("url").toString());
			}

			// Else set URL for current profile photo
			else {
				if(!img_url.isEmpty()) {
					user.setPicUrl(img_url);
				}
				else {
					user.setPicUrl("");
				}
			}

			if(!user.getLinkedIn().startsWith("https://")){
				user.setLinkedIn("https://" + user.getLinkedIn());
			}
			if(!user.getGithub().startsWith("https://")){
				user.setGithub("https://" + user.getGithub());
			}
			// Check if new user registering
			if(userRepository.countByUsername(user.getUsername()) < 1) {
				userService.saveStudent(user);
			}
			// Else re-save user //
			else{
				//userRepository.save(user);
				return "redirect:/exists";
			}
		} catch (IOException e){
			e.printStackTrace();
			model.addAttribute("imageURL", img_url);
			return "userinfo";
		}
		try{
			notificationService.SendNotification(user);
		}catch (MailException e){

		}

		return "redirect:/";
	}

	@RequestMapping("/myprofile")
	public String GetMyProfile(Model model) {
		User user = userService.getCurrentUser();
		model.addAttribute("user", user);
		model.addAttribute("img_url", user.getPicUrl());
		return "profile";
	}

	@RequestMapping ("/profile/{id}")
	public String GetProfile(@PathVariable("id") long id, Model model){
		model.addAttribute("user",
				userRepository.findById(id).get());
        model.addAttribute("user", userService.getCurrentUser());
		return "profile";
	}

	@RequestMapping ("/updateToAdmin/{id}")
	public String UpdateToAdminRole(@PathVariable("id") long id, Model model){
		userService.SetNewRole("Admin", userRepository.findById(id).get());
		model.addAttribute("users", userRepository.findAll());
        model.addAttribute("user", userService.getCurrentUser());
		return "admin/users";
	}

	@RequestMapping ("/updateToInstructor/{id}")
	public String UpdateToInstructorRole(@PathVariable("id") long id,
									   Model model){
		userService.SetNewRole("Instructor", userRepository.findById(id).get());
		model.addAttribute("users", userRepository.findAll());
        model.addAttribute("user", userService.getCurrentUser());
		return "admin/users";
	}

	@RequestMapping ("/updateToStudent/{id}")
	public String UpdateToStudentRole(@PathVariable("id") long id, Model model){
		userService.SetNewRole("Student", userRepository.findById(id).get());
		model.addAttribute("users", userRepository.findAll());
        model.addAttribute("user", userService.getCurrentUser());
		return "admin/users";
	}

	//====================================================================
	// COURSES
	//====================================================================

	@GetMapping("/courseform")
	public String GetCourseForm(Model model) {
		model.addAttribute("course", new Course());
        model.addAttribute("user", userService.getCurrentUser());
		return "admin/courseform";
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
            model.addAttribute("user", userService.getCurrentUser());
			return "courses";
		}
	}

	@RequestMapping("/mycourses")
	public String GetAllCourses(Model model){
		model.addAttribute("courses",
				userService.GetAllCoursesByUser(userService.getCurrentUser()));
		model.addAttribute("displayEnroll",false);
		model.addAttribute("displayDrop",true);
        model.addAttribute("user", userService.getCurrentUser());
		return "courses";
	}

	@RequestMapping("/enrollcourse")
	public String GetListOfCoursesToEnroll(Model model) {
		ArrayList<Course> userCourses =
				userService.GetAllCoursesByUser(userService.getCurrentUser());
		Iterable<Course> allCourses = courseRepository.findAll();
		Iterator<Course> courseIterator = allCourses.iterator();

		ArrayList<Course> courses = new ArrayList<>();

		while(courseIterator.hasNext()) {
			Course course = courseIterator.next();
			if(!userCourses.contains(course)){
				courses.add(course);
			}
			courseIterator.remove();
		}

		model.addAttribute("displayEnroll",true);
		model.addAttribute("displayDrop",false);
		model.addAttribute("courses", courses);
        model.addAttribute("user", userService.getCurrentUser());
		return "courses";
	}

	@RequestMapping("/enrollcourse/{id}")
	public String AddToMyCourses(@PathVariable("id") long id, Model model) {
		Course course = courseRepository.findById(id).get();
		User user = userService.getCurrentUser();

		UserCourse userCourse = new UserCourse(user,
				userService.getCurrentRole(user), course);
		userCourseRepository.save(userCourse);
        model.addAttribute("user", userService.getCurrentUser());
		return "redirect:/mycourses";
	}
    @RequestMapping("/dropcourse/{id}")
    public String RemoveFromMyCourses(@PathVariable("id") long id, Model model) {
        Course course = courseRepository.findById(id).get();
        User user = userService.getCurrentUser();
        Role role = userService.getCurrentRole(user);
        UserCourse userCourse = userCourseRepository.findByCourseAndRoleAndUser(course,
                role, user);
        userCourseRepository.delete(userCourse);
        model.addAttribute("user", userService.getCurrentUser());

        return "redirect:/mycourses";
    }
	@RequestMapping("/currentCourses")
	public String GetCurrentCourses(Model model) {

		model.addAttribute("courses", getCourses("current"));
        model.addAttribute("user", userService.getCurrentUser());
		return "courses";
	}

	@RequestMapping("/pastCourses")
	public String GetPastCourses(Model model) {
		model.addAttribute("courses", getCourses("past"));
        model.addAttribute("user", userService.getCurrentUser());
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

		if(role.getRole().equals("Admin")) {
			return courseRepository.findAllBySemester(semester);
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
        model.addAttribute("user", userService.getCurrentUser());

		return "coursedetail";
	}

	@RequestMapping ("/disableCourse/{id}")
	public String DisableCourse(@PathVariable("id") long id, Model model){
		Course course = courseRepository.findById(id).get();
		course.setActive(false);
		courseRepository.save(course);
        model.addAttribute("user", userService.getCurrentUser());
		return "redirect:/enrollcourse";
	}

	@RequestMapping ("/enableCourse/{id}")
	public String EnableCourse(@PathVariable("id") long id, Model model){
		Course course = courseRepository.findById(id).get();
		course.setActive(true);
		courseRepository.save(course);
        model.addAttribute("user", userService.getCurrentUser());
		return "redirect:/enrollcourse";
	}

	//====================================================================
	// POSTS
	//====================================================================

	@RequestMapping ("/newpost/{id}")
	public String GetPostForm(@PathVariable("id") long id, Model model){
		model.addAttribute("course_id", id);
		model.addAttribute("post", new Post());
        model.addAttribute("user", userService.getCurrentUser());
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
            model.addAttribute("user", userService.getCurrentUser());
			return "posts";
		}
	}

	@RequestMapping("/posts/{id}")
	public String GetPosts(@PathVariable("id") long id, Model model){
		Course course = courseRepository.findById(id).get();
		model.addAttribute("course", course);
		model.addAttribute("posts", postRepository.findAllByCourse(course));
        model.addAttribute("user", userService.getCurrentUser());
		return "posts";
	}
	public ArrayList<PrivateMessage> messages;
	@RequestMapping ("/newmessage/{id}")
	public String GetmessageForm(@PathVariable("id") long id,@ModelAttribute PrivateMessage privateMessage, Model model){
		model.addAttribute("course_id", id);
//		model.addAttribute("receiver_id", id);
//		model.addAttribute("sender_id", id);
		model.addAttribute("users", userRepository.findByRole(2));
        model.addAttribute("user", userService.getCurrentUser());
		model.addAttribute("privatemessage", new PrivateMessage());
		return "messageform";
	}
	@PostMapping("/messageform")
	public String SendMessage(@Valid @ModelAttribute("privatemessage") PrivateMessage privateMessage,
						  BindingResult result, Model model, HttpServletRequest request){
		if (request.getParameterValues("receiver_id") != null) {
			String[] recieverFs = null;
			recieverFs = request.getParameterValues("receiver_id");
			//List<Long> list = new ArrayList<Long>();
			List<Long> list = new ArrayList<Long>();
			for(String recieverF : recieverFs) {
				try {
					list.add(Long.parseLong(recieverF.trim()));
			} catch(Exception e) {
				// log about conversion error
			}
				System.out.println(list);
			}

			for (int i = 0; i < list.size(); i++) {
			 PrivateMessage pm = new PrivateMessage();

				if (result.hasErrors()) {
					return "messageform";
				} else {

					Course course =
							courseRepository.findById(new Long(request.getParameter(
									"course_id"))).get();
					User receiver = userRepository.findById(list.get(i)).get();
					pm.setCourse(course);
					pm.setReceiver(receiver);
					User sender = userService.getCurrentUser();
					pm.setSender(sender);
					pm.setDateSent(LocalDateTime.now());
				    String message = privateMessage.getMessageContent();
				    pm.setMessageContent(message);
					String subject = privateMessage.getSubject();
					pm.setSubject(subject);
					privateMessageRepository.save(pm);
					model.addAttribute("course", course);
					model.addAttribute("users", userRepository);
                    model.addAttribute("user", userService.getCurrentUser());
					model.addAttribute("privateMessage", privateMessageRepository.findAllBySender(sender));
				}
			}
		}

			return "redirect:/mycourses";

	}

	@RequestMapping("/messages")
	public String GetMessages(Model model){
		//Course course = courseRepository.findById(id).get();
		User receiver = userService.getCurrentUser();
		model.addAttribute("receiver", receiver);
		//model.addAttribute("course", course);
        model.addAttribute("user", userService.getCurrentUser());
		model.addAttribute("privatemessages",privateMessageRepository.findAllBySender(receiver));
		return "inbox";
	}
}
