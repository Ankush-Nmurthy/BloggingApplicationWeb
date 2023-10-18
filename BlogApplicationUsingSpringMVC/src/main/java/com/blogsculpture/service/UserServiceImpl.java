package com.blogsculpture.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.blogsculpture.model.User;
import com.blogsculpture.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public User loginUser(String email, String password) {
		// System.out.println("inside the login user method of class UserServiceImpl");
		User user = userRepository.findByEmail(email);
		// System.out.println(user);
		if (user != null && encoder.matches(password, user.getPassword())) {
			return user;
		}
		return null;
	}

	@Override
	public User registerUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		return userRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User findById(Integer id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			return user.get();
		}
		return null;
	}

	@Override
	public User edit(User user) {
		return userRepository.save(user);
	}

	@Override
	public void deleteById(Integer id) {
		userRepository.deleteById(id);
	}

}
