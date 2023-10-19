package com.blogsculpture.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.blogsculpture.model.User;
import com.blogsculpture.service.AdminService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {

	@Autowired
	public AdminService adminService;

	@GetMapping("/admin")
	public String getAdminPageOfWebApplication() {
		return "/admin/index";
	}

	@GetMapping("/admin/users")
	public String getTable(Model model) {
		List<User> users = adminService.findAllUsers();
		model.addAttribute("users", users);
		return "/admin/user_table";
	}

}
