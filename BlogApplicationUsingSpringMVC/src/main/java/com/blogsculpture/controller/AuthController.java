package com.blogsculpture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.blogsculpture.dto.UserSignupDTO;
import com.blogsculpture.event.RegistrationCompleteEvent;
import com.blogsculpture.model.User;
import com.blogsculpture.service.UserService;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping("/login")
    public String getLoginForm() {
        return "login";
    }

    @GetMapping("/registration")
    public String registerUser(Model model) {
        model.addAttribute("userDto", new UserSignupDTO());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("userDto") UserSignupDTO userDto, Model model) {
        User user = userService.registerUser(userDto);
        publisher.publishEvent(new RegistrationCompleteEvent(user, ""));
        return "redirect:/registration?success";
    }
}
