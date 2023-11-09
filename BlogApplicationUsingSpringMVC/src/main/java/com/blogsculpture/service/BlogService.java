package com.blogsculpture.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.blogsculpture.model.Blog;
import com.blogsculpture.model.Blog.Status;
import com.blogsculpture.model.Blog.AccessType;

public interface BlogService {
	List<Blog> getBlogsByUserId(Integer UserId);

	List<Blog> getBlogByAuthorName(String authorName);

	void addBlog(Blog blog);

	Blog getBlog(Integer id);

	Integer getLikesForABlog(Integer blogId);

	List<Blog> findTopTrendingBlog(Integer limit);

	Page<Blog> findAllBlogs(Pageable pageable);

	Page<Blog> getBlogsByCategory(String category, Integer limit);

	// These methods are used in User and Admin section.
	Page<Blog> findByAuthorUserIdOrderByDateDesc(Integer authorUserId, Pageable pageable);

	Page<Blog> findByCategoryAndAuthorUserIdOrderByDateDesc(String category, Integer authorUserId, Integer pagenumber);

	Page<Blog> findByCategoryAndAccessTypeAndAuthorUserIdOrderByDateDesc(String category, Blog.AccessType status,
			Integer authorUserId, Integer pagenumber);

	Page<Blog> findByCategoryAndStatusAndAuthorUserIdOrderByDateDesc(String category, Blog.Status status,
			Integer authorUserId, Integer pagenumber);

	Page<Blog> findByCategoryAndAccessTypeAndStatusAndAuthorUserIdOrderByDateDesc(String category,
			Blog.AccessType accessType, Blog.Status status, Integer userId, Integer pagenumber);
}
