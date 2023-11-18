package com.blogsculpture.controller;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.blogsculpture.appconfig.CustomUser;
import com.blogsculpture.exceptions.NotFoundException;
import com.blogsculpture.model.Blog;
import com.blogsculpture.model.User;
import com.blogsculpture.service.BlogService;
import com.blogsculpture.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private BlogService blogService;

	@GetMapping("/user")
	public String getHomePage(Model model) {
		userService.setUsernameAndProfileImageToModel(model);
		return "user/index";
	}

	@GetMapping("/user/edit")
	public String updateUser(Model model) {
		userService.setUsernameAndProfileImageToModel(model);
		CustomUser authenticatedUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User user = userService.findById(authenticatedUser.getUser().getUserId());
		model.addAttribute("userDto", user);
		return "user/edit_details";
	}

	@PostMapping("/user/edit")
	public String updateUser(@ModelAttribute("userDto") User userDto, Model model) {
		userService.setUsernameAndProfileImageToModel(model);
		userService.updateUser(userDto, userDto.getUserId());
		return "redirect:/user/edit";
	}



	// currently this method is not in use because the alternate url is
	// "/blog/{currentpage}?category=all"
	@GetMapping("/user/blog")
	public String getBlogPage(Model model) {
		userService.setUsernameAndProfileImageToModel(model);
		return "commonblogLayout/blog_page";
	}

}
