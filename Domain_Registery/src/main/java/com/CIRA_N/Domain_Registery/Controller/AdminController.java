package com.CIRA_N.Domain_Registery.Controller;

import ch.qos.logback.core.model.Model;
import com.CIRA_N.Domain_Registery.Services.DomainService;
import com.CIRA_N.Domain_Registery.Services.UserService;
import com.CIRA_N.Domain_Registery.model.Domain;
import com.CIRA_N.Domain_Registery.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DomainService domainService;

    @Autowired
    private UserService userService;

    // ── ADMIN DASHBOARD ──────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {

        List<Domain> allDomains = domainService.getAllDomains();
        List<Domain> expiring = domainService.getExpiringDomains();
        List<User> allUsers = userService.getAllUsers();

        long active = allDomains.stream()
                .filter(d -> d.getStatus().name().equals("ACTIVE")).count();
        long expired = allDomains.stream()
                .filter(d -> d.getStatus().name().equals("EXPIRED")).count();

        model.addAttribute("totalDomains", allDomains.size());
        model.addAttribute("activeDomains", active);
        model.addAttribute("expiredDomains", expired);
        model.addAttribute("expiringCount", expiring.size());
        model.addAttribute("totalUsers", allUsers.size());
        model.addAttribute("recentDomains", allDomains.stream().limit(5).toList());

        return "admin/dashboard";  // loads templates/admin/dashboard.html
    }

    // ── ALL DOMAINS ──────────────────────────────────────────────────
    @GetMapping("/domains")
    public String allDomains(Model model) {
        model.addAttribute("domains", domainService.getAllDomains());
        return "admin/all-domains";
    }

    // ── EXPIRING DOMAINS ─────────────────────────────────────────────
    @GetMapping("/domains/expiring")
    public String expiringDomains(Model model) {
        model.addAttribute("domains", domainService.getExpiringDomains());
        return "admin/expiring-domains";
    }

    // -- DELETE DOMAIN
    @PostMapping("/domain/{id}/delete")
    public String deleteDomain(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ){
        try {
            domainService.deleteDomain(id);
            redirectAttributes.addFlashAttribute("sucess", "Domain deleted");
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/domains";
    }
// ── ALL USERS ────────────────────────────────────────────────────
@GetMapping("/users")
public String allUsers(Model model) {
    model.addAttribute("users", userService.getAllUsers());
    return "admin/all-users";
}

// -- DEACTIVATE USER
@PostMapping("/users/{id}/deactivate")
public String deactivateUser(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes
){
    try{
        userService.deactivateUser(id);
        redirectAttributes.addFlashAttribute("succes", "User deactivated");
    }catch (RuntimeException e){
        redirectAttributes.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/admin/users";
}

// ── ACTIVATE USER ────────────────────────────────────────────────
@PostMapping("/users/{id}/activate")
public String activateUser(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes) {

    try {
        userService.activateUser(id);
        redirectAttributes.addFlashAttribute("success", "User activated.");
    } catch (RuntimeException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/admin/users";
}
}