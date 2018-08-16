package ApgNetworking.services;

import ApgNetworking.models.*;
import ApgNetworking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Component
public class DataLoader implements CommandLineRunner{

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserCourseRepository userCourseRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PrivateMessageRepository privateMessageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
        Run method will be executed after the application context is
        loaded and right before the Spring Application run method is
        completed.
     */
    @Override
    public void run(String... strings) throws Exception{
        loadData();
    }

    private void loadData(){
        System.out.println("Loading data...");
        addUserAndRoles();
        addCourses();
        addPosts();
        addMessages();
    }

    private void addUserAndRoles(){
        roleRepository.save(new Role("Admin"));
        roleRepository.save(new Role("Student"));
        roleRepository.save(new Role("Instructor"));

        Role adminRole = roleRepository.findByRole("Admin");
        Role studentRole = roleRepository.findByRole("Student");
        Role instructorRole = roleRepository.findByRole("Instructor");

        User user = new User("example@gmail.com","Alton","Henley","admin",
                "password","/img/cat.jpg","https://www.linkedin" +
                ".com/school/montgomery-college/","https://github.com/jbcmay2018",true);
        user.setRoles(Arrays.asList(adminRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        user = new User("example@gmail.com","Dave","Wolf","instructor","password","/img/cat.jpg","https://www.linkedin" +
                ".com/school/montgomery-college/","https://github.com/jbcmay2018",true);
        user.setRoles(Arrays.asList(instructorRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        user = new User("example@gmail.com","Melissa","Fung","maf","password","/img/cat.jpg","https://www.linkedin" +
                ".com/school/montgomery-college/","https://github.com/jbcmay2018",true);
        user.setRoles(Arrays.asList(studentRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        user = new User("example@gmail.com","Josemy","Joseph","jj","password","/img/cat.jpg","https://www.linkedin" +
                ".com/school/montgomery-college/","https://github.com/jbcmay2018",true);
        user.setRoles(Arrays.asList(studentRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        user = new User("example@gmail.com","Bart","Simpson","bs",
                "password","/img/cat.jpg","https://www.linkedin" +
                ".com/school/montgomery-college/","https://github.com/jbcmay2018",true);
        user.setRoles(Arrays.asList(studentRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    private void addCourses(){
        Collection<UserCourse> userCourses = new ArrayList<>();

        Course course = new Course("Java", "current", "10045",
                "Intro to Java",true);
        userCourses.add(new UserCourse(userRepository.findByUsername
                ("instructor"),course));
        userCourses.add(new UserCourse(userRepository.findByUsername
                ("maf"),course));
        userCourses.add(new UserCourse(userRepository.findByUsername
                ("jj"),course));
        userCourses.add(new UserCourse(userRepository.findByUsername
                ("bs"),course));
        course.setUserCourses(userCourses);
        courseRepository.save(course);

        course = new Course("Database", "past", "100456",
                "Intro to database",true);
        userCourses.add(new UserCourse(userRepository.findByUsername
                ("instructor"),course));
        userCourses.add(new UserCourse(userRepository.findByUsername
                ("jj"),course));
        course.setUserCourses(userCourses);
        courseRepository.save(course);
    }

    private void addPosts(){
        Post post = new Post("All About Dave4","https://github.com/dave45678",
                "Lorem ipsum dolor sit amet, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        post.setCourse(courseRepository.findByCrn("10045"));
        post.setUser(userRepository.findByUsername
                ("instructor"));
        postRepository.save(post);

        post = new Post("All About Dave5","https://github.com/dave45678",
                "Lorem ipsum dolor sit amet, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        post.setCourse(courseRepository.findByCrn("10045"));
        post.setUser(userRepository.findByUsername
                ("maf"));
        postRepository.save(post);

        post = new Post("All About Dave6","https://github.com/dave45678",
                "Lorem ipsum dolor sit amet, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        post.setCourse(courseRepository.findByCrn("10045"));
        post.setUser(userRepository.findByUsername
                ("jj"));
        postRepository.save(post);

        post = new Post("All About Dave7","https://github.com/dave45678",
                "Lorem ipsum dolor sit amet, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        post.setCourse(courseRepository.findByCrn("10045"));
        post.setUser(userRepository.findByUsername
                ("bs"));
        postRepository.save(post);

        post = new Post("All About Dave8","https://github.com/dave45678",
                "Lorem ipsum dolor sit amet, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        post.setCourse(courseRepository.findByCrn("10045"));
        post.setUser(userRepository.findByUsername
                ("jj"));
        postRepository.save(post);

        post = new Post("All About Dave9?","https://github.com/dave45678",
                "Lorem ipsum dolor sit amet, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        post.setCourse(courseRepository.findByCrn("10045"));
        post.setUser(userRepository.findByUsername
                ("bs"));
        postRepository.save(post);
    }

    private void addMessages(){
        PrivateMessage privateMessage = new PrivateMessage("Hello World",
                LocalDateTime.now(),"Lorem ipsum dolor sit amet, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        privateMessage.setCourse(courseRepository.findByCrn("10045"));
        privateMessage.setSender(userRepository.findByUsername("instructor"));
        privateMessage.setReceiver(userRepository.findByUsername("jj"));
        privateMessageRepository.save(privateMessage);

        privateMessage = new PrivateMessage("",
                LocalDateTime.now(),"Lorem ipsum dolor sit amet, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        privateMessage.setCourse(courseRepository.findByCrn("10045"));
        privateMessage.setSender(userRepository.findByUsername("jj"));
        privateMessage.setReceiver(userRepository.findByUsername("instructor"));
        privateMessageRepository.save(privateMessage);

        privateMessage = new PrivateMessage("Hello from Melissa",
                LocalDateTime.now(),"Lorem ipsum dolor sit amet, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        privateMessage.setCourse(courseRepository.findByCrn("10045"));
        privateMessage.setSender(userRepository.findByUsername("maf"));
        privateMessage.setReceiver(userRepository.findByUsername("jj"));
        privateMessageRepository.save(privateMessage);

        privateMessage = new PrivateMessage("Hello from Bart",
                LocalDateTime.now(),"Lorem ipsum dolor sit amet, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        privateMessage.setCourse(courseRepository.findByCrn("10045"));
        privateMessage.setSender(userRepository.findByUsername("bs"));
        privateMessage.setReceiver(userRepository.findByUsername("jj"));
        privateMessageRepository.save(privateMessage);
    }


}