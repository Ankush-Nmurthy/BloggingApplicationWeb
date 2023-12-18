package com.blogsculpture.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.blogsculpture.appconfig.CustomUser;
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

	// users Dash-board graphs
	@GetMapping("/user/graph/blogsByCategory")
	@ResponseBody
	public ResponseEntity<?> countBlogsWrittenByUserBasedOnUserId() {
		CustomUser authenticatedUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Integer userId = authenticatedUser.getUser().getUserId();
		Map<String, Long> countBlogsWrittenByUserBasedOnUserId = userService
				.countBlogsWrittenByUserBasedOnUserId(userId);
		return new ResponseEntity<Map<String, Long>>(countBlogsWrittenByUserBasedOnUserId, HttpStatus.OK);
	}

	@GetMapping("/user/graph/blogsByAccessTypeAndStatus")
	@ResponseBody
	public ResponseEntity<?> countingBlogsBasedOnAccessTypeOfUser() {
		CustomUser authenticatedUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Integer userId = authenticatedUser.getUser().getUserId();
		Map<String, Long> result = userService.countingBlogsBasedOnAccessTypeAndStatusOfUser(userId);
		return new ResponseEntity<Map<String, Long>>(result, HttpStatus.OK);
	}
}
