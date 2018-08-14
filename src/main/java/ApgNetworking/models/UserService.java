package ApgNetworking.models;

import ApgNetworking.repositories.RoleRepository;
import ApgNetworking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
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
