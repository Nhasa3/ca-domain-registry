package com.CIRA_N.Domain_Registery.Controller;

import com.CIRA_N.Domain_Registery.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage(){
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterPage(){
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes){

        //Check passwords match
        if(!password.equals(confirmPassword)){
            redirectAttributes.addFlashAttribute("error", "Password do not match");
            return "redirect:/register";
        }

        try{
            userService.registerUser(fullName, email, password);
            redirectAttributes.addFlashAttribute("success", "Account created! Please log in");
            return "redirect:/login";
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

}
