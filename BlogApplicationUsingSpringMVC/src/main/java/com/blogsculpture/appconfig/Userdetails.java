package com.blogsculpture.appconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.blogsculpture.model.User;
import com.blogsculpture.repo.UserRepository;

@Service
public class Userdetails implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found for the given username");
		} else {
			return new CustomUser(user);
		}
	}

}
