package com.blogsculpture.controller;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectController {

	@RequestMapping("/redirectUserBasedOnRole")
	public String redirectUserBasedOnRole(Authentication authentication) {
		// System.out.println("inside RedirectController class");
		if (authentication != null) {
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			if (authorities.stream().anyMatch((auth) -> auth.getAuthority().equals("ROLE_ADMIN"))) {
				return "redirect:/admin";
			} else if (authorities.stream().anyMatch((auth) -> auth.getAuthority().equals("ROLE_USER"))) {
				return "redirect:/user";
			}
		}
		return "login";
	}

}