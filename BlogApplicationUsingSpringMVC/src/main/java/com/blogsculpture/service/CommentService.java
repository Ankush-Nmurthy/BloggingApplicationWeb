package com.blogsculpture.service;

import com.blogsculpture.model.User;

public interface CommentService {
	String addCommentForABlog(Integer blogId,String comment,User user);

	String deleteCommentForABlog(Integer blogId, User user);
}
