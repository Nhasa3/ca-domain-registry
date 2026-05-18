package com.CIRA_N.Domain_Registery.Services;

import com.CIRA_N.Domain_Registery.Repository.UserRepository;
import com.CIRA_N.Domain_Registery.model.Role;
import com.CIRA_N.Domain_Registery.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ── REGISTER NEW USER ─────────────────────────────────────────────
    public User registerUser(String fullName, String email, String password) {

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("An account with this email already exists.");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email.toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.REGISTRANT);
        user.setActive(true);
        user.setCreatedAt(LocalDate.now());

        return userRepository.save(user);
    }

    // ── GET USER BY EMAIL ─────────────────────────────────────────────
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    // ── GET USER BY ID ────────────────────────────────────────────────
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    // ── GET ALL USERS (Admin only) ────────────────────────────────────
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ── DEACTIVATE USER (Admin only) ──────────────────────────────────
    public void deactivateUser(Long userId) {
        User user = getUserById(userId);
        user.setActive(false);
        userRepository.save(user);
    }

    // ── ACTIVATE USER (Admin only) ────────────────────────────────────
    public void activateUser(Long userId) {
        User user = getUserById(userId);
        user.setActive(true);
        userRepository.save(user);
    }

    // ── COUNT USER'S DOMAINS ──────────────────────────────────────────
    public long getDomainCount(User user) {
        return user.getDomains().size();
    }
}