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
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

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

    

}
