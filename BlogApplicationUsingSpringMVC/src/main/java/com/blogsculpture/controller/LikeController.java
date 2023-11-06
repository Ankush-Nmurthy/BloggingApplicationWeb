package com.blogsculpture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.blogsculpture.appconfig.CustomUser;
import com.blogsculpture.service.BlogService;
import com.blogsculpture.service.LikeService;

@Controller
public class LikeController {

	@Autowired
	public LikeService likeService;

	@Autowired
	public BlogService blogService;

	@PostMapping("/blog/like/{id}")
	@Transactional
	public String likeABlog(@PathVariable Integer id) {
		CustomUser authUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		likeService.likeABlog(id, authUser.getUser());
		return "redirect:/blogview/"+id;
	}

	@PostMapping("/blog/dislike/{id}")
	@Transactional
	public String dislikeABlog(@PathVariable Integer id) {
		CustomUser authUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		likeService.dislikeABlog(id, authUser.getUser());
		return "redirect:/blogview/" + id;
	}

}
