package com.blogsculpture.service;

import java.util.List;

import org.springframework.ui.Model;

import com.blogsculpture.dto.UserSignupDTO;
import com.blogsculpture.model.User;

public interface UserService {
	User loginUser(String email, String password);

	List<User> findAll();

	User findById(Integer id);

	String deactivateUserAccount(Integer userid);

	User registerUser(UserSignupDTO user);

	String updateUser(User user, Integer id);

	User edit(User user);

	void deleteById(Integer id);

	void setUsernameAndProfileImageToModel(Model model);

	User findUserByUserName(String username);

	void saveUser(User user);
}
