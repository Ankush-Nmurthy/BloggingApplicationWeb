package com.blogsculpture.service;

import java.util.List;
import com.blogsculpture.model.Blog;

public interface BlogService {
	List<Blog> getBlogsByUserId(Integer UserId);

	List<Blog> getBlogsByCategory(String category);

	List<Blog> getBlogByAuthorName(String authorName);

	void createBlog(Blog blog);

	List<Blog> getAllBlogs();
}
