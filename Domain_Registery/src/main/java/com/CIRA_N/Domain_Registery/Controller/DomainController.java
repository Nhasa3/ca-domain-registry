package com.CIRA_N.Domain_Registery.Controller;

import ch.qos.logback.core.model.Model;
import com.CIRA_N.Domain_Registery.Services.DomainService;
import com.CIRA_N.Domain_Registery.Services.UserService;
import com.CIRA_N.Domain_Registery.model.Domain;
import com.CIRA_N.Domain_Registery.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/domains")
public class DomainController {

    @Autowired
    private DomainService domainService;

    @Autowired
    private UserService userService;

    // -- Get current logged-in user
    private User getCurrentUser(Principal principal){
        return userService.getUserByEmail(principal.getName());
    }

    // Check Domain availability (from homepage search)
    @GetMapping("/search")
    public String searchDomain(
            @RequestParam String name,
            Model model
    ){
        String domainName = name.toLowerCase().trim();
        boolean available = domainService.isAvailable(domainName);

        model.addAttribute("domainName", domainName);
        model.addAttribute("available", available);

        // If taken, show the domain details
        if(!available){
            try{
                Domain domain = domainService.getDomainByName(domainName);
                model.addAttribute("domain", domain);
            }catch (Exception ignored) {}
        }
        return "search-result";  // Loads templates/serach-result.html
    }

    // Show domain registration form
    @GetMapping("/register")
    public String showRegisterForm(
            @RequestParam(required = false) String name,
            Model model
    ){
        model.addAttribute("domainName", name != null ? name : "");
        return "domain/register";
    }

    // Handle Domain Registration submit
    @PostMapping("/register")
    public String registerDomain(
            @RequestParam String domainName,
            Principal principal,
            RedirectAttributes redirectAttributes
    ){
        try{
            User currentUser = getCurrentUser(principal);
            domainService.registerDomain(domainName, currentUser);
            redirectAttributes.addFlashAttribute("success",
                    "Domain '" + domainName + "' registered successfully!");
            return "redirect:/domains/my";
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/domains/register";
        }
    }

    // my domain page
    @GetMapping("/my")
    public String myDomains(Principal principal, Model model){
        User currentUser = getCurrentUser(principal);
        List<Domain> domains = domainService.getDomainsForUser(currentUser);

        // Summary counts for the dashboard cards
        long active = domains.stream()
                .filter(d -> d.getStatus().name().equals("ACTIVE")).count();
        long expired = domains.stream()
                .filter(d -> d.getStatus().name().equals("EXPIRED")).count();

        model.addAttribute("domains", domains);
        model.addAttribute("activeCount", active);
        model.addAttribute("expiredCount", expired);
        model.addAttribute("totalCount", domains.size());

        return "domain/my-domains";  // loads templates/domain/my-domains.html
    }

    // User dashboard
    @GetMapping("/dashboard")
    public String userDashboard(Principal principal, Model model){

        User currentUser = getCurrentUser(principal);
        List<Domain> domains = domainService.getDomainsForUser(currentUser);

        // Expiring soon (within 30 days)
        List<Domain> expiringSoon = domainService.getExpiringDomains()
                .stream()
                .filter(d -> d.getOwner().getId().equals(currentUser.getId()))
                .toList();

        model.addAttribute("user", currentUser);
        model.addAttribute("totalDomains", domains.size());
        model.addAttribute("expiringSoon", expiringSoon.size());
        model.addAttribute("recentDomains", domains.stream().limit(5).toList());

        return "dashboard/user-dashboard";
    }

}
