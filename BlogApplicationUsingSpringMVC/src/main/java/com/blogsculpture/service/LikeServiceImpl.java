package com.blogsculpture.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blogsculpture.exceptions.BlogExceptions;
import com.blogsculpture.model.Blog;
import com.blogsculpture.model.Like;
import com.blogsculpture.model.User;
import com.blogsculpture.repo.BlogRepository;
import com.blogsculpture.repo.LikeRepository;
import com.blogsculpture.repo.UserRepository;

@Service
public class LikeServiceImpl implements LikeService {

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private LikeRepository likeRepository;

	@Autowired
	private UserRepository userRepository;

//	todo:look at the like and dislike functionality because they are not working as expected.

	@Override
	public String likeABlog(Integer blogId, User user) {
		Blog blog = blogRepository.findById(blogId)
				.orElseThrow(() -> new BlogExceptions("Blog Not found for the given Blog ID"));
		boolean userAlreadyLiked = user.getLikes().stream().anyMatch((e) -> e.getBlog().getBlogId() == blogId);
		// System.out.println(userAlreadyLiked);
		if (!userAlreadyLiked) {
			Like like = new Like();
			like.setBlog(blog);
			like.setLikeDate(LocalDateTime.now());
			like.setUser(user);
			// Attach the 'like' entity to the persistence context
			like = likeRepository.save(like);

			// Add the like to the blog and user
			blog.getLikes().add(like);
			user.getLikes().add(like);

			// Update the blog and user entities
			blogRepository.save(blog);
			userRepository.save(user);

			return "Blog liked successfully";
		}

		return "you have already liked the blog.";
	}

	@Override
	public String dislikeABlog(Integer id, User user) {
		Blog blog = blogRepository.findById(id)
				.orElseThrow(() -> new BlogExceptions("Blog Not found for the given Blog ID"));
		List<Like> list = blog.getLikes().stream().filter((like) -> Objects.equals(like.getBlog().getBlogId(), blog.getBlogId())
				&& Objects.equals(like.getUser().getUserId(), user.getUserId())).toList();
		if (!list.isEmpty()) {
			Like like = list.get(0);
			boolean removeIf = blog.getLikes().removeIf((likes) -> Objects.equals(likes.getId(), like.getId()));
			// System.out.println(removeIf);
			removeIf = user.getLikes().removeIf((likes) -> Objects.equals(likes.getId(), like.getId()));
			// System.out.println(removeIf);
			// System.out.println(user.getLikes());
			likeRepository.deleteById(like.getId());
			blogRepository.save(blog);
			userRepository.save(user);
		}
		return "Like removed Successfully.";
	}

}
