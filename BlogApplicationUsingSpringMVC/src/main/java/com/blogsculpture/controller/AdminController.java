package com.blogsculpture.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.blogsculpture.appconfig.CustomUser;
import com.blogsculpture.model.User;
import com.blogsculpture.service.AdminService;
import com.blogsculpture.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {

	@Autowired
	public AdminService adminService;

	@Autowired
	private UserService userService;

	/**
	 * Method returns the Admin page
	 * 
	 * @return
	 */
	@GetMapping("/admin")
	public String adminHome(Model model) {
		adminService.setUsernameAndProfileImageToModel(model);
		int[] activeUsers = adminService.activeAndDeactiveUsersInPlatform();
		Integer totalAdministrators = adminService.administratorCount();
		model.addAttribute("totaladmin", totalAdministrators);
		model.addAttribute("activeUser", activeUsers[0]);
		model.addAttribute("deactiveUser", activeUsers[1]);
		return "admin/index";
	}

	@GetMapping("/admin/users")
	public String getUsersTable(Model model) {
		List<User> users = adminService.findAllUsers();
		adminService.setUsernameAndProfileImageToModel(model);
		model.addAttribute("users", users);
		return "admin/user_table";
	}

	@GetMapping("/admin/admins")
	public String getAdminsTable(Model model) {
		List<User> users = adminService.findAllUsers();
		adminService.setUsernameAndProfileImageToModel(model);
		model.addAttribute("users", users);
		return "admin/admin_table";
	}

	@RequestMapping(value = "/admin/deactivateaccount/{id}", method = { RequestMethod.GET })
	public String deactivateUserAccount(@PathVariable Integer id, Model model) {
		adminService.setUsernameAndProfileImageToModel(model);
		adminService.deactivateUserAccount(id);
		User user = userService.findById(id);
		if (user.getRole().equals("ROLE_ADMIN")) {
			return "redirect:/admin/admins";
		}
		return "redirect:/admin/users";
	}

	@RequestMapping(value = "/admin/activateaccount/{id}", method = { RequestMethod.GET })
	public String activateUserAccount(@PathVariable Integer id, Model model) {
		adminService.setUsernameAndProfileImageToModel(model);
		adminService.activateUserAccount(id);
		User user = userService.findById(id);
		if (user.getRole().equals("ROLE_ADMIN")) {
			return "redirect:/admin/admins";
		}
		return "redirect:/admin/users";
	}

	@GetMapping(value = "/admin/toggelRole/{id}")
	public String updateUserRoleAsAdmin(@PathVariable Integer id, Model model) {
		adminService.setUsernameAndProfileImageToModel(model);
		adminService.updateUserRoleToAdmin(id);
		User user = userService.findById(id);
		if (user.getRole().equals("ROLE_ADMIN")) {
			return "redirect:/admin/users";
		}
		return "redirect:/admin/admins";
	}

	@GetMapping("/admin/blog")
	public String getBlogPage() {
		return "admin/blog_page";
	}

	@GetMapping("/admin/edit")
	public String updateUser(Model model) {
		userService.setUsernameAndProfileImageToModel(model);
		CustomUser authenticatedUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User user = userService.findById(authenticatedUser.getUser().getUserId());
		model.addAttribute("userDto", user);
		return "admin/edit_details";
	}

	@PostMapping("/admin/edit")
	public String updateUser(@ModelAttribute("userDto") User userDto, Model model) {
		userService.setUsernameAndProfileImageToModel(model);
		userService.updateUser(userDto, userDto.getUserId());
		return "redirect:/admin/edit";
	}

	// methods for visualization charts;
	@GetMapping("/admin/graph/activeUsers")
	@ResponseBody
	public ResponseEntity<?> graphForActiveAndDeactivatedUsers() {
		int[] activeandDeactivedUser = adminService.activeAndDeactiveUsersInPlatform();
		Map<String, Integer> userstatus = new HashMap<>();
		userstatus.put("Active Users", activeandDeactivedUser[0]);
		userstatus.put("InActive Users", activeandDeactivedUser[1]);
		return new ResponseEntity<Map<String, Integer>>(userstatus, HttpStatus.OK);
	}

	@GetMapping("/admin/graph/blogsCategoryWise")
	@ResponseBody
	public ResponseEntity<?> graphForNumberOfBlogsCategoryWise() {
		Map<String, Long> graphForNumberOfBlogsCategoryWise = adminService.graphForNumberOfBlogsCategoryWise();
		System.out.println(graphForNumberOfBlogsCategoryWise);
		return new ResponseEntity<>(graphForNumberOfBlogsCategoryWise, HttpStatus.OK);
	}

	@GetMapping("/admin/graph/userRegisteredMonthWise")
	@ResponseBody
	public ResponseEntity<?> graphForUserRegistrationMonthWise() {
		Map<String, Long> graphForUserRegistrationMonthWise = adminService.graphForUserRegistrationMonthWise();
		System.out.println(graphForUserRegistrationMonthWise);
		return new ResponseEntity<>(graphForUserRegistrationMonthWise, HttpStatus.OK);
	}
	
	
	@GetMapping("/admin/graph/blogRadarGraphValues")
	@ResponseBody
	public ResponseEntity<?> graphForGettingCountOfBlogBasedOnAccessType() {
		Map<String, Long> graphForUserRegistrationMonthWise = adminService.graphForGettingCountOfBlogBasedOnAccessType();
		System.out.println(graphForUserRegistrationMonthWise);
		return new ResponseEntity<>(graphForUserRegistrationMonthWise, HttpStatus.OK);
	}
	
	// method for testing purpose.
	@GetMapping("/persent/user")
	@ResponseBody
	public String getLoggedinUser() {
		return (String) SecurityContextHolder.getContext().getAuthentication().getName();
	}
}
