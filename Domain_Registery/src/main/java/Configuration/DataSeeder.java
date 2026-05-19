package Configuration;

import com.CIRA_N.Domain_Registery.Repository.UserRepository;
import com.CIRA_N.Domain_Registery.model.Role;
import com.CIRA_N.Domain_Registery.model.User;
import org.hibernate.boot.model.process.internal.UserTypeResolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args){

        //Only create admin if it doesn't exist yet
        if(!userRepository.existsByEmail("admin@registery.ca")){
            User admin = new User();
            admin.setFullName("System Admin");
            admin.setEmail("admin@registery.ca");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            admin.setCreatedAt(LocalDate.now());

            userRepository.save(admin);
            System.out.println("Admin account created: admin@registery.ca");
        }
    }
}
