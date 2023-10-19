package com.blogsculpture.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

}
