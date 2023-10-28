package com.blogsculpture.service;

import java.util.List;
import org.springframework.ui.Model;
import com.blogsculpture.model.User;

public interface AdminService {
	public List<User> findAllUsers();
	String deactivateUserAccount(Integer userId);
	String activateUserAccount(Integer userId);
	String updateUserRoleToAdmin(Integer userId);
	void setUsernameAndProfileImageToModel(Model model);
}
