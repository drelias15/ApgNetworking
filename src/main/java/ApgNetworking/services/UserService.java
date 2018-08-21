package ApgNetworking.services;

import ApgNetworking.models.Course;
import ApgNetworking.models.Role;
import ApgNetworking.models.User;
import ApgNetworking.models.UserCourse;
import ApgNetworking.repositories.RoleRepository;
import ApgNetworking.repositories.UserCourseRepository;
import ApgNetworking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.CollectionTable;
import java.util.*;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserCourseRepository userCourseRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository)
    {
        this.userRepository=userRepository;
    }

    public User findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }

    // Returns the currently logged in User object
    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentusername = authentication.getName();
        User user = userRepository.findByUsername(currentusername);
        return user;
    }

    // receives User object, returns current role of that user
    public Role getCurrentRole(User user) {
        //Role role = new Role();
        Iterator<Role> it = user.getRoles().iterator();
       // it.next();
//        while(it.hasNext()){
//            role = it.next();
//            it.remove();
//        }
        return it.next();
    }

    // Receives role id and role name, returns collection of roles
    public void SetNewRole(String role_name, User user) {
        Role role = new Role();
        long role_id = roleRepository.findByRole(role_name).getId();
        role.setId(role_id);
        role.setRole(role_name);
        Collection<Role> roles = new HashSet<>();
        roles.add(role);

        user.setRoles(roles);
        userRepository.save(user);
    }

    public ArrayList<Course> GetAllCoursesByUser(User user) {
        Collection<UserCourse> userCourses =
                userCourseRepository.findAllByUser(user);
        Iterator<UserCourse> userCourseIterator = userCourses.iterator();

        ArrayList<Course> courses = new ArrayList<>();

        while(userCourseIterator.hasNext()) {
            Course course = userCourseIterator.next().getCourse();
            courses.add(course);
            userCourseIterator.remove();
        }

        return courses;
    }

    public void saveAdmin(User user)
    {
        user.setRoles(Arrays.asList(roleRepository.findByRole("Admin")));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void saveStudent(User user)
    {
        List list = new ArrayList(Arrays.asList(roleRepository.findByRole("Student")));
        user.setRoles(list);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void saveInstructor(User user)
    {
        user.setRoles(Arrays.asList(roleRepository.findByRole("Instructor")));
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

}
