package com.CIRA_N.Domain_Registery.Controller;

import ch.qos.logback.core.model.Model;
import com.CIRA_N.Domain_Registery.Services.DomainService;
import com.CIRA_N.Domain_Registery.Services.UserService;
import com.CIRA_N.Domain_Registery.model.Domain;
import com.CIRA_N.Domain_Registery.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
