package ApgNetworking.services;

import ApgNetworking.models.Role;
import ApgNetworking.models.User;
import ApgNetworking.repositories.RoleRepository;
import ApgNetworking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner{

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
        Run method will be executed after the application context is
        loaded and right before the Spring Application run method is
        completed.
     */
    @Override
    public void run(String... strings) throws Exception{
        //loadData();
    }

    private void loadData(){
        System.out.println("Loading data...");

        roleRepository.save(new Role("Admin"));
        roleRepository.save(new Role("Student"));
        roleRepository.save(new Role("Instructor"));

        Role adminRole = roleRepository.findByRole("Admin");
        Role studentRole = roleRepository.findByRole("Student");
        Role instructorRole = roleRepository.findByRole("Instructor");

        User user = new User("melissaafung@gmail.com","Melissa","Fung","admin","password",true);
        user.setRoles(Arrays.asList(adminRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);


    }

}