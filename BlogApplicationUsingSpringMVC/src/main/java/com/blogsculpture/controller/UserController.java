package com.blogsculpture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.blogsculpture.appconfig.CustomUser;
import com.blogsculpture.dto.UserSignupDTO;
import com.blogsculpture.event.RegistrationCompleteEvent;
import com.blogsculpture.model.User;
import com.blogsculpture.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationEventPublisher publisher;

	// @GetMapping("/login")
	// public String getLoginForm() {
	// return "/login";
	// }

	// @GetMapping("/registration")
	// public String registerUser(Model model) {
	// model.addAttribute("userDto", new UserSignupDTO());
	// return "/registration";
	// }

	// @PostMapping("/registration")
	// public String registerUser(@ModelAttribute("userDto") UserSignupDTO userDto,
	// Model model) {
	// User user = userService.registerUser(userDto);
	// publisher.publishEvent(new RegistrationCompleteEvent(user, ""));
	// return "redirect:registration?success";
	// }

	@GetMapping("/user")
	public String getHomePage(Model model) {
		userService.setUsernameAndProfileImageToModel(model);
		return "/user/index";
	}

	@GetMapping("/user/edit")
	public String updateUser(Model model) {
		userService.setUsernameAndProfileImageToModel(model);
		CustomUser authenticatedUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User user = userService.findById(authenticatedUser.getUser().getUserId());
		model.addAttribute("userDto", user);
		return "/user/edit_details";
	}

	@PostMapping("/user/edit")
	public String updateUser(@ModelAttribute("userDto") User userDto, Model model) {
		userService.setUsernameAndProfileImageToModel(model);
		userService.updateUser(userDto, userDto.getUserId());
		return "redirect:/user/edit";
	}

	@GetMapping("/user/blog")
	public String getBlogPage(Model model) {
		userService.setUsernameAndProfileImageToModel(model);
		return "/commonblogLayout/blog_page";
	}
}
