package com.blogsculpture.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.blogsculpture.appconfig.CustomUser;
import com.blogsculpture.dto.UserSignupDTO;
import com.blogsculpture.exceptions.UserNotFoundException;
import com.blogsculpture.model.Address;
import com.blogsculpture.model.User;
import com.blogsculpture.repo.AddressRepository;
import com.blogsculpture.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AddressRepository addressRepository;

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
	public User registerUser(UserSignupDTO userDto) {
		User user = new User(userDto.getName(), userDto.getEmail(), userDto.getNationality(), userDto.getReligion(),
				LocalDateTime.now(), userDto.getPassword(), userDto.getGender(),userDto.getMobileno());
		Address address = new Address();
		address.setCity(userDto.getAddressDTO().getCity());
		address.setState(userDto.getAddressDTO().getState());
		address.setCountry(userDto.getAddressDTO().getCountry());
		user.setPassword(encoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		user.setActive(true);
		address.setUser(user);
		userRepository.save(user);
		addressRepository.save(address);
		return user;
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User findById(Integer id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			throw new UserNotFoundException("user with give is not present : "+id);
		}
		return user.get();
	}

	@Override
	public User edit(User user) {
		return userRepository.save(user);
	}

	@Override
	public void deleteById(Integer id) {
		userRepository.deleteById(id);
	}

	@Override
	public String deactivateUserAccount(Integer userid) {
		Optional<User> userById = userRepository.findById(userid);
		userById.get().setActive(false);
		return "User deactivated successfully";
	}
	

	@Override
	public String updateUser(User user, Integer id) {
		User foundUser = findById(id);
		if(user.getName() != null) foundUser.setName(user.getName());
		if(user.getNationality() != null) foundUser.setNationality(user.getNationality());
		if(user.getMobileno() != null) foundUser.setMobileno(user.getMobileno());
		if(user.getGender() != null) foundUser.setGender(user.getGender());
		if(user.getReligion() != null) foundUser.setReligion(user.getReligion());
		if(user.getAddress().getCity() != null) foundUser.getAddress().setCity(user.getAddress().getCity());
		if(user.getAddress().getCountry() != null) foundUser.getAddress().setCountry(user.getAddress().getCountry());
		if(user.getAddress().getState() != null) foundUser.getAddress().setState(user.getAddress().getState());
		foundUser.setUpdatedAt(LocalDateTime.now());
		userRepository.save(foundUser);
		addressRepository.save(foundUser.getAddress());
		return "User updated successfully.";
	}
	
	@Override
	public void setUsernameAndProfileImageToModel(Model model) {
		CustomUser authenticatedUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("username",authenticatedUser.getUser().getName());
		model.addAttribute("profileImage",authenticatedUser.getUser().getEncoded());
	}
}
