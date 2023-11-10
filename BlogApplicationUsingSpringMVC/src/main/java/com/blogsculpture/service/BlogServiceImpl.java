package com.blogsculpture.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.blogsculpture.appconfig.CustomUser;
import com.blogsculpture.model.Blog;
import com.blogsculpture.model.Like;
import com.blogsculpture.model.User;
import com.blogsculpture.repo.BlogRepository;
import com.blogsculpture.model.Blog.Status;
import com.blogsculpture.model.Blog.AccessType;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public List<Blog> getBlogsByUserId(Integer UserId) {
        return blogRepository.findByAuthorUserId(UserId);
    }

    @Override
    public Page<Blog> getBlogsByCategory(String category, Integer pagenumber) {
        // System.out.println("inside get blog by category");
        Pageable page = PageRequest.of(pagenumber, 6);
        return blogRepository.findByCategoryOrderByDateDesc(category, page);
    }

    @Override
    public List<Blog> getBlogByAuthorName(String authorName) {
        return blogRepository.findByAuthorName(authorName);
    }

    @Override
    public void addBlog(Blog blog) {
        blogRepository.save(blog);
    }

    @Override
    public Blog getBlog(Integer id) {
        return blogRepository.findById(id).orElse(null);
    }

    @Override
    public Integer getLikesForABlog(Integer blogId) {
        Set<Like> likes = blogRepository.findAllLikesByBlogId(blogId);
        return likes.size();
    }

    @Override
    public List<Blog> findTopTrendingBlog(Integer limit) {
        Pageable p = PageRequest.of(limit, 6);
        return blogRepository.findTopTrendingBlog(p);
    }

    @Override
    public Page<Blog> findAllBlogs(Pageable pageable) {
        // System.out.println("inside findall method");
        Page<Blog> findAll = blogRepository.findAll(pageable);
        return findAll;
    }


    // The below 5 method is common for both admin and user dashboard pages.
    @Override
    public Page<Blog> findByAuthorUserIdOrderByDateDesc(Integer authorUserId, Pageable pageable) {
        return blogRepository.findByAuthorUserIdOrderByDateDesc(authorUserId, pageable);
    }

    @Override
    public Page<Blog> findByCategoryAndAuthorUserIdOrderByDateDesc(String category, Integer authorUserId,
                                                                   Integer pagenumber) {
        Pageable pageable = PageRequest.of(pagenumber - 1, 3);
        return blogRepository.findByCategoryAndAuthorUserIdOrderByDateDesc(category, authorUserId, pageable);
    }

    @Override
    public Page<Blog> findByCategoryAndAccessTypeAndAuthorUserIdOrderByDateDesc(String category, AccessType status,
                                                                                Integer authorUserId, Integer pagenumber) {
        Pageable pageable = PageRequest.of(pagenumber - 1, 3);
        return blogRepository.findByCategoryAndAccessTypeAndAuthorUserIdOrderByDateDesc(category, status, authorUserId,
                pageable);
    }

    @Override
    public Page<Blog> findByCategoryAndStatusAndAuthorUserIdOrderByDateDesc(String category, Status status,
                                                                            Integer authorUserId, Integer pagenumber) {
        Pageable pageable = PageRequest.of(pagenumber - 1, 3);
        return blogRepository.findByCategoryAndStatusAndAuthorUserIdOrderByDateDesc(category, status, authorUserId,
                pageable);
    }

    @Override
    public Page<Blog> findByCategoryAndAccessTypeAndStatusAndAuthorUserIdOrderByDateDesc(String category,
                                                                                         AccessType accessType, Status status, Integer userId, Integer pagenumber) {
        Pageable pageable = PageRequest.of(pagenumber - 1, 3);
        return blogRepository.findByCategoryAndAccessTypeAndStatusAndAuthorUserIdOrderByDateDesc(category, accessType,
                status, userId, pageable);
    }

}
