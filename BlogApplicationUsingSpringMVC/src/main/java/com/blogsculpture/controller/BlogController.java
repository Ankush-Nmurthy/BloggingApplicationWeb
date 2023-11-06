package com.blogsculpture.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.blogsculpture.appconfig.CustomUser;
import com.blogsculpture.model.Blog;
import com.blogsculpture.model.Blog.AccessType;
import com.blogsculpture.model.Blog.Status;
import com.blogsculpture.model.User;
import com.blogsculpture.repo.BlogRepository;
import com.blogsculpture.service.BlogService;
import com.blogsculpture.service.UserService;
import com.blogsculpture.exceptions.NotFoundException;

@Controller
public class BlogController {

	@Autowired
	private BlogService blogService;

	@Autowired
	private UserService userService;

	@GetMapping("/index")
	public String getIndexPageOfWebApplication() {
		return "/index";
	}

	// for this end point there is no need to get authenticated and ie (no login
	// needed).
	@GetMapping("/")
	public String homeBlogPageHandler() {
		return "commonblogLayout/blogIndex";
	}

	@GetMapping("/blog/redirectUser")
	public String redirectUserToAccountFromBlogPage(Authentication authentication) {
		System.out.println("inside blog/redirestUser class");
		if (authentication != null) {
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			if (authorities.stream().anyMatch((auth) -> auth.getAuthority().equals("ROLE_ADMIN"))) {
				return "redirect:/admin";
			} else if (authorities.stream().anyMatch((auth) -> auth.getAuthority().equals("ROLE_USER"))) {
				return "redirect:/user";
			}
		}
		return "redirect:/blog";
	}

	@GetMapping("/addblog")
	public String createNewBlog(Model model) {
		model.addAttribute("blogEntity", new Blog());
		return "commonblogLayout/add_blog";
	}

	@PostMapping("/addblog")
	public String createNewBlog(@RequestParam("file") MultipartFile file, @ModelAttribute("blogEntity") Blog blogEntity,
			@RequestParam("content") String content) {
		CustomUser loggedInUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = loggedInUser.getUser();
		try {
			if (!file.isEmpty()) {
				blogEntity.setData(file.getBytes());
				blogEntity.setContent(content);
				blogEntity.setEncoded(Base64.getEncoder().encodeToString(blogEntity.getData()));
				blogEntity.setAuthor(user);
				user.getBlogs().add(blogEntity);
				blogService.addBlog(blogEntity);
				userService.saveUser(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/addblog?success=true";
	}

	@GetMapping("/blogview/{id}")
	public String viewBlogByBlogId(Model model, @PathVariable Integer id) {
		Blog blog = blogService.getBlog(id);
		blog.setEncoded(Base64.getEncoder().encodeToString(blog.getData()));
		model.addAttribute("blogDto", blog);
		model.addAttribute("author", blog.getAuthor().getName().toUpperCase());
		model.addAttribute("likeCount", blogService.getLikesForABlog(id));
		return "commonblogLayout/view_blog";
	}

	@GetMapping("/editable/{id}")
	public String editBlogByBlogId(Model model, @PathVariable Integer id) {
		Blog blog = blogService.getBlog(id);
		blog.setEncoded(Base64.getEncoder().encodeToString(blog.getData()));
		model.addAttribute("blogEntity", blog);

		return "commonblogLayout/edit_blog";
	}

	@PostMapping("/editBlog/{id}")
	public String editBlogByBlogId(@RequestParam("file") MultipartFile file,
			@ModelAttribute("blogEntity") Blog blogEntity, @RequestParam("content") String content,
			@PathVariable Integer id) {
		Blog blogEntity2 = blogService.getBlog(id);
		CustomUser loggedInUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = loggedInUser.getUser();
		if (blogEntity2.getAuthor().getUserId() != user.getUserId()) {
			return "redirect:/editable/" + id + "?error=You are not the author of this blog to edit";
		}
		try {
			if (!file.isEmpty()) {
				blogEntity2.setData(file.getBytes());
				blogEntity2.setEncoded(Base64.getEncoder().encodeToString(blogEntity2.getData()));
			}
			blogEntity2.setAccessType(blogEntity.getAccessType());
			blogEntity2.setCategory(blogEntity.getCategory());
			blogEntity2.setStatus(blogEntity.getStatus());
			blogEntity2.setContent(content);
			blogEntity2.setUpdated(new Date());
			blogEntity2.setTitle(blogEntity.getTitle());
			blogService.addBlog(blogEntity2);
		} catch (IOException e) {
			return "error";
		}
		return "redirect:/editable/" + id + "?success = blog edited successfully";
	}

	// This end point is used by two pages
	// 1. blog_page(which is the user blogpage)
	// 2. blogIndex(which can accessed without login);
	@GetMapping("/blog/{currentPage}")
	public String getCategory(@RequestParam("category") String category, @PathVariable Integer currentPage,
			Model model) {
		if (currentPage == null || currentPage <= 0) {
			currentPage = 1;
		}
		Pageable pageable  = PageRequest.of(currentPage-1, 6);
		Page<Blog> page = category.equals("all") ? blogService.findAllBlogs(pageable)
				: blogService.getBlogsByCategory(category, currentPage - 1);
		if (page.isEmpty())
			throw new NotFoundException("Content for this page is empty!");

		List<Blog> perPageBlogs = page.getContent().stream().map(e -> {
			e.setEncoded(Base64.getEncoder().encodeToString(e.getData()));
			return e;
		}).collect(Collectors.toList());

		System.out.println(perPageBlogs.size());

		model.addAttribute("blogDto", perPageBlogs);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentcategory", category);
		return "commonblogLayout/blog_page";
	}

	@GetMapping("/blog/trending/{limit}")
	@ResponseBody
	public List<Blog> findTrendingBlogs(@PathVariable Integer limit) {
		List<Blog> findTopTrendingBlog = blogService.findTopTrendingBlog(limit);
		return findTopTrendingBlog;
	}

	// http://localhost:9090/blogservices/1?category=all&accesstype=null&status=null
	@GetMapping("/blogservices/{currentpage}")
	public String getBlogServicesPage(Model model, @PathVariable("currentpage") Integer currentPage,
			@RequestParam(required = false) String category, @RequestParam(required = false) String accesstype,
			@RequestParam(required = false) String status) {

		// using the current logged in user in-order to get his
		// blog's and to display on his user dash-board.
		CustomUser loggedInUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = loggedInUser.getUser();
		userService.setUsernameAndProfileImageToModel(model);

		AccessType accessTypeEnum = accesstype == null ? AccessType.UNKNOWN : AccessType.valueOf(accesstype);
		Status statusEnum = status == null ? Status.UNKNOWN : Status.valueOf(status);

		Page<Blog> page = null;
		if (currentPage == null || currentPage <= 0) {
			currentPage = 1;
		}

		// debugging;
		System.out.println("accessTypeEnum : " + accessTypeEnum + " statusEnum : " + statusEnum + " Category : "
				+ category + " currentPage : " + currentPage);

		// 1. by default take all the request as category all;
		if (category.equals("all")
				|| category == null && accessTypeEnum == AccessType.UNKNOWN && statusEnum == Status.UNKNOWN) {
			Pageable pageable = PageRequest.of(currentPage-1, 3);
			page = blogService.findAllBlogs(pageable);
		}
		// 2. only category;
		else if (!category.equals("all")
				|| category != null && accessTypeEnum == AccessType.UNKNOWN && statusEnum == Status.UNKNOWN) {
			page = blogService.findByCategoryAndAuthorUserIdOrderByDateDesc(category, user.getUserId(), currentPage);
		}
		// 3. category and access-type;
		else if (!category.equals("all")
				|| category != null && accessTypeEnum != AccessType.UNKNOWN && statusEnum == Status.UNKNOWN) {
			page = blogService.findByCategoryAndAccessTypeAndAuthorUserIdOrderByDateDesc(category, accessTypeEnum,
					user.getUserId(), currentPage);
		}

		// 4, category and status;
		else if (!category.equals("all")
				|| category != null && accessTypeEnum == AccessType.UNKNOWN && statusEnum != Status.UNKNOWN) {
			page = blogService.findByCategoryAndStatusAndAuthorUserIdOrderByDateDesc(category, statusEnum,
					user.getUserId(), currentPage);
		}

		// 5. category, status. access-type
		else if (!category.equals("all")
				|| category != null && accessTypeEnum != AccessType.UNKNOWN && statusEnum != Status.UNKNOWN) {
			page = blogService.findByCategoryAndAccessTypeAndStatusAndAuthorUserIdOrderByDateDesc(category,
					accessTypeEnum, statusEnum, user.getUserId(), currentPage);
		}

		if (page.isEmpty())
			throw new NotFoundException("Content for this page is empty!");

		List<Blog> perPageBlogs = page.getContent().stream().map(e -> {
			e.setEncoded(Base64.getEncoder().encodeToString(e.getData()));
			return e;
		}).collect(Collectors.toList());

		System.out.println(perPageBlogs.size());

		model.addAttribute("blogDto", perPageBlogs);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentcategory", category);
		return "user/blogservices";
	}

//	@Autowired
//	private BlogRepository blogRepository;
//
//	@GetMapping("/blogservices/{category}/{accesstype}/{page}")
//	@ResponseBody
//	public List<Blog> getBlogservicesCurrentpage(@PathVariable(required = false) String category,
//			@PathVariable("accesstype") AccessType accesstype, @PathVariable("page") Integer page) {
//		Pageable pages = PageRequest.of(page - 1, 3);
//		Page<Blog> blogs = blogRepository.findByCategoryAndAccessType(category, accesstype, pages);
//		return blogs.getContent();
//	}
}
