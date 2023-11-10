package com.blogsculpture.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.blogsculpture.appconfig.CustomUser;
import com.blogsculpture.model.User;
import com.blogsculpture.repo.BlogRepository;
import com.blogsculpture.repo.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {

	
	public UserRepository userRepository;
	
	public BlogRepository blogRepository;
	
	@Autowired
	public AdminServiceImpl(UserRepository userRepository, BlogRepository blogRepository) {
		super();
		this.userRepository = userRepository;
		this.blogRepository = blogRepository;
	}

	@Override
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}
	
	@Override
	public String deactivateUserAccount(Integer userId) {
		Optional<User> userById = userRepository.findById(userId);
		userById.get().setActive(false);
		userRepository.save(userById.get());
		return "User deactivated successfully";
	}
	
	@Override
	public String activateUserAccount(Integer userId) {
		Optional<User> userById = userRepository.findById(userId);
		userById.get().setActive(true);
		userRepository.save(userById.get());
		return "User activated successfully";
	}

	@Override
	public String updateUserRoleToAdmin(Integer userId) {
		Optional<User> userById = userRepository.findById(userId);
		String role = userById.get().getRole().equals("ROLE_ADMIN") ? "ROLE_USER" : "ROLE_ADMIN";
		// System.out.println(role);
		userById.get().setRole(role);
		userById.get().setUpdatedAt(LocalDateTime.now());
		userRepository.save(userById.get());
		return "User's Role updated successfully";
	}
	
	@Override
	public void setUsernameAndProfileImageToModel(Model model) {
		CustomUser authenticatedUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("username",authenticatedUser.getUser().getName());
		model.addAttribute("profileImage",authenticatedUser.getUser().getEncoded());
	}


}
