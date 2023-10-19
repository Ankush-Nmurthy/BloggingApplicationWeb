package com.blogsculpture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.blogsculpture.appconfig.Userdetails;
import com.blogsculpture.dto.UserSignupDTO;
import com.blogsculpture.event.RegistrationCompleteEvent;
import com.blogsculpture.model.User;
import com.blogsculpture.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private Userdetails userdetails;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute("user") User user, Authentication session) {
		System.out.println(user);
		User authenticatedUser = userService.loginUser(user.getEmail(), user.getPassword());
		userdetails.loadUserByUsername(user.getEmail());
		SecurityContextHolder.getContext().setAuthentication((Authentication) authenticatedUser);
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(name);
		System.out.println(authenticatedUser);
		if (authenticatedUser != null) {
			return "redirect:index";
		}
		System.out.println("authenticated....!");
		return "index";
	}

	@GetMapping("/registration")
	public String register(Model model) {
		model.addAttribute("userDto", new UserSignupDTO());
		return "/registration";
	}

	@PostMapping("/registration")
	public String showRegistrationForm(@ModelAttribute("userDto") UserSignupDTO userDto, Model model) {
		User user = userService.registerUser(userDto);
		publisher.publishEvent(new RegistrationCompleteEvent(user, ""));
		return "redirect:registration?success";
	}

//	@PostMapping("/registration")
//	public String register(@ModelAttribute("user") User user, Model model) {
//		userService.registerUser(user);
//		publisher.publishEvent(new RegistrationCompleteEvent(user, ""));
//		return "redirect:registration?success";
//	}

//	@GetMapping("/registration")
//	public String showRegistrationForm(Model model) {
//		model.addAttribute("userDto", new UserSignupDTO());
//		return "registration";
//	}

	@GetMapping("/check")
	public String showsecurityContext() {
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
		return "registration";
	}
}
