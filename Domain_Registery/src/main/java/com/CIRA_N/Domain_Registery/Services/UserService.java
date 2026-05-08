package com.CIRA_N.Domain_Registery.Services;

import com.CIRA_N.Domain_Registery.Repository.UserRepository;
import com.CIRA_N.Domain_Registery.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String fullName, String email, String password){

        // Check if email already in use
        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("An account this email already exists.");
        }

        User user = new User();
        user.setName(fullName);
        user.setEmail(email.toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.REGISTRANT);
        user.setActive(true);

        return userRepository.save(user);
    }

    // ── GET USER BY EMAIL ───────────────────────────────────────────
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    // ── GET USER BY ID ──────────────────────────────────────────────
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    // ── GET ALL USERS (Admin only) ──────────────────────────────────
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ── ACTIVATE USER (Admin only) ──────────────────────────────────
    public void activateUser(Long userId) {
        User user = getUserById(userId);
        user.setActive(true);
        userRepository.save(user);
    }

    // ── COUNT USER'S DOMAINS ────────────────────────────────────────
    // Used in admin user table to show domain count per user
    public long getDomainCount(User user) {
        return user.getDomains().size();
    }

}
