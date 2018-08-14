package ApgNetworking.controllers;

import ApgNetworking.configurations.CloudinaryConfig;
import ApgNetworking.models.Post;
import ApgNetworking.models.User;
import ApgNetworking.models.Course;
import ApgNetworking.repositories.CourseRepository;
import ApgNetworking.repositories.PostRepository;
import ApgNetworking.repositories.RoleRepository;
import ApgNetworking.repositories.UserRepository;
import ApgNetworking.models.UserService;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class MainController {
	@Autowired
	private UserService userService;
	@Autowired
	private CourseRepository courserepo;
	@Autowired
	private UserRepository  userRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	CloudinaryConfig cloudc;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private PostRepository postRepository;


	@RequestMapping("/")
	public String index() {
		return"index";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/userinfo")
	public String userinfo(Model model) {
		model.addAttribute("apgUser", new User());
		model.addAttribute("actCourses", courserepo.findAll());
		return "userinfo";
	}

	@PostMapping("/userinfo")
	public String displayform(@Valid@ModelAttribute("apgUser")User apgUser, BindingResult result, Model model,@RequestParam("file")MultipartFile file) {
		if (result.hasErrors()||file.isEmpty()) {
			model.addAttribute("actCourses", courserepo.findAll());
			return "userinfo";
		} else {
			try {
				Map uploadResult =  cloudc.upload(file.getBytes(),
						ObjectUtils.asMap("resourcetype", "auto"));
				apgUser.setPicUrl(uploadResult.get("url").toString());

				if(!apgUser.getLinkedIn().startsWith("https://")){
					apgUser.setLinkedIn("https://"+apgUser.getLinkedIn());
				}
				if(!apgUser.getGithub().startsWith("https://")){
					apgUser.setGithub("https://"+apgUser.getGithub());
				}

				userService.saveStudent(apgUser);
			} catch (IOException e){
				e.printStackTrace();
				model.addAttribute("actCourses", courserepo.findAll());
				return "redirect:/userinfo";
			}
			return "login";
		}
	}
	@RequestMapping ("/profilepage")
	public String profilePage(Authentication authentication, Model model){
		model.addAttribute("currUser",userRepo.findByUsername(authentication.getName()));
		return "profilepage";
	}

	@GetMapping("/courseform")
	public String courseform(Model model) {
		model.addAttribute("course", new Course());
		return "courseform";
	}

	@PostMapping("/courseform")
	public String showCourse(@Valid @ModelAttribute("course") Course course, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "courseform";
		} else {

			courserepo.save(course);
			model.addAttribute("allCourses", courserepo.findAll());
			return "courses";
		}

	}
	@RequestMapping("/courses")
		public String courses(Model model){
		model.addAttribute("allCourses", courserepo.findAll());
		return "courses";
	}
	@GetMapping("/postform")
	public String postform(Model model){
		model.addAttribute("post", new Post());
		return "postform";
	}
	@PostMapping("/postform")
	public String savePost(@Valid @ModelAttribute("post") Post post, BindingResult result, Model model, Authentication authentication){
		if (result.hasErrors()) {
			return "postform";
		} else {
			post.setUser(userRepo.findByUsername(authentication.getName()));
			if(!post.getLink().startsWith("https://")){
				post.setLink("https://"+post.getLink());
			}
			postRepository.save(post);
			model.addAttribute("posts", postRepository.findAll());
			return "posts";
		}
	}
	@RequestMapping("/posts")
	public String posts(Model model){
		model.addAttribute("posts", postRepository.findAll());
		return "posts";
	}
}
