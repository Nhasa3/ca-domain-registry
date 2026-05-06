package com.CIRA_N.Domain_Registery.Controller;

import com.CIRA_N.Domain_Registery.Services.DomainService;
import com.CIRA_N.Domain_Registery.Services.UserService;
import com.CIRA_N.Domain_Registery.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

    


}
