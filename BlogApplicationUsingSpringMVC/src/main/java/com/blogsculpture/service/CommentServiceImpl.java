package com.blogsculpture.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.blogsculpture.exceptions.BlogExceptions;
import com.blogsculpture.model.Blog;
import com.blogsculpture.model.Comment;
import com.blogsculpture.model.User;
import com.blogsculpture.repo.BlogRepository;
import com.blogsculpture.repo.CommentRepository;
import com.blogsculpture.repo.UserRepository;

public class CommentServiceImpl implements CommentService {

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Override
	public String addCommentForABlog(Integer blogId, String comment, User user) {
		Blog blog = blogRepository.findById(blogId)
				.orElseThrow(() -> new BlogExceptions("Blog Not found for the given Blog ID"));
		Comment newComment = new Comment();
		newComment.setCommentedOn(LocalDateTime.now());
		newComment.setComment(comment);
		newComment.setUser(user);
		newComment.setBlog(blog);
		commentRepository.save(newComment);
		return "redirect:/blog/" + blogId; // Redirect back to the blog page
	}

	@Override
	public String deleteCommentForABlog(Integer blogId, User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
