package com.CIRA_N.Domain_Registery.Controller;

import com.CIRA_N.Domain_Registery.Services.DomainService;
import com.CIRA_N.Domain_Registery.Services.UserService;
import com.CIRA_N.Domain_Registery.model.Domain;
import com.CIRA_N.Domain_Registery.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private DomainService domainService;

    @Autowired
    private UserService userService;

    // ── USER DASHBOARD — mapped to /dashboard ─────────────────────────
    @GetMapping("/templates/dashboard")
    public String userDashboard(Principal principal, Model model) {

        User currentUser = userService.getUserByEmail(principal.getName());
        List<Domain> domains = domainService.getDomainsForUser(currentUser);

        List<Domain> expiringSoon = domainService.getExpiringDomains()
                .stream()
                .filter(d -> d.getOwner().getId().equals(currentUser.getId()))
                .toList();

        long active = domains.stream()
                .filter(d -> d.getStatus().name().equals("ACTIVE")).count();

        model.addAttribute("user", currentUser);
        model.addAttribute("totalDomains", domains.size());
        model.addAttribute("activeCount", active);
        model.addAttribute("expiringSoon", expiringSoon.size());
        model.addAttribute("recentDomains", domains.stream().limit(5).toList());

        return "templates/dashboard/user-dashboard";
    }
}