package com.blogsculpture.service;

import com.blogsculpture.model.User;

public interface LikeService {
	
	String likeABlog(Integer id, User user);

	String dislikeABlog(Integer id, User user);
	
}
