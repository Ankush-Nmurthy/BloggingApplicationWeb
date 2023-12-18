package com.blogsculpture.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.blogsculpture.appconfig.CustomUser;
import com.blogsculpture.model.Blog.AccessType;
import com.blogsculpture.model.Blog.Status;
import com.blogsculpture.model.User;
import com.blogsculpture.repo.BlogRepository;
import com.blogsculpture.repo.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {

	public UserRepository userRepository;

	public BlogRepository blogRepository;

	public UserService userservice;

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
		CustomUser authenticatedUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		model.addAttribute("username", authenticatedUser.getUser().getName());
		model.addAttribute("profileImage", authenticatedUser.getUser().getEncoded());
	}

	@Override
	public int[] activeAndDeactiveUsersInPlatform() {
		int[] arr = new int[2];
		Integer activeAccount = userRepository.countByIsActive(true);
		Integer deactiveAccount = userRepository.countByIsActive(false);
		arr[0] = activeAccount;
		arr[1] = deactiveAccount;
		return arr;
	}

	@Override
	public Integer administratorCount() {
		return userRepository.administratorsOfApplication();
	}

	@Override
	public Map<String, Long> graphForNumberOfBlogsCategoryWise() {
		Map<String, Long> map = new HashMap<>();
		List<Object[]> countingBlogByCategory = blogRepository.countingBlogByCategory();
		for (Object[] obj : countingBlogByCategory) {
			String key = (String) obj[0];
			Long value = (Long) obj[1];
			map.put(key, value);
		}
		return map;
	}

	@Override
	public Map<String, Long> graphForUserRegistrationMonthWise() {
		Map<String, Long> map = new HashMap<>();
		List<Object[]> graphForUserRegistrationMonthWise = userRepository.graphForUserRegistrationMonthWise();
		for (Object[] obj : graphForUserRegistrationMonthWise) {
			String key = obj[0] + "/" + obj[1];
			Long value = (Long) obj[2];
			map.put(key, value);
		}
		System.out.println(map);
		return map;
	}

	@Override
	public Map<String, Long> graphForGettingCountOfBlogBasedOnAccessType() {
		// for public blog;
		Map<String, Long> map = new HashMap<>();

		List<Object[]> privateBlog = blogRepository.countingBlogsBasedOnAccessType(AccessType.PUBLIC);
		map.put("Public", (Long) privateBlog.get(0)[1]);
		List<Object[]> PublicBlog = blogRepository.countingBlogsBasedOnAccessType(AccessType.PRIVATE);
		map.put("Private", (Long) PublicBlog.get(0)[1]);
		List<Object[]> draftBlogs = blogRepository.countingBlogsBasedOnStatus(Status.COMPLETED);
		map.put("Draft", (Long) draftBlogs.get(0)[1]);
		List<Object[]> completedBlogs = blogRepository.countingBlogsBasedOnStatus(Status.DRAFT);
		map.put("Completed", (Long) completedBlogs.get(0)[1]);
		
		return map;
	}

}