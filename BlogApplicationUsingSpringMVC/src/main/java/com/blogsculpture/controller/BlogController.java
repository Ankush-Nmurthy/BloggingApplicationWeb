package com.blogsculpture.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.blogsculpture.model.Blog;
import com.blogsculpture.service.BlogService;

@Controller
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/index")
    public String getIndexPageOfWebApplication() {
        return "/index";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Blog> blogs = blogService.getAllBlogs();
        model.addAttribute("blogs", blogs);
        return "dashboard";
    }

    @GetMapping("/create-blog")
    public String showCreateBlogForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "create-blog";
    }

    @PostMapping("/create-blog")
    public String createBlog(@ModelAttribute("blog") Blog blog) {
        blogService.createBlog(blog);
        return "redirect:/dashboard";
    }

    @GetMapping("/user/{userId}")
    public String getBlogsByUserId(@PathVariable Integer userId, Model model) {
        List<Blog> blogs = blogService.getBlogsByUserId(userId);
        model.addAttribute("blogs", blogs);
        return "blog-list";
    }

    @GetMapping("/category/{category}")
    public String getBlogsByCategory(@PathVariable String category, Model model) {
        List<Blog> blogs = blogService.getBlogsByCategory(category);
        model.addAttribute("blogs", blogs);
        return "blog-list";
    }

    // Get blogs by author name
    @GetMapping("/author/{authorName}")
    public String getBlogByAuthorName(@PathVariable String authorName, Model model) {
        List<Blog> blogs = blogService.getBlogByAuthorName(authorName);
        model.addAttribute("blogs", blogs);
        return "blog-list";
    }

    // Get all blogs
    @GetMapping("/all")
    public String getAllBlogs(Model model) {
        List<Blog> blogs = blogService.getAllBlogs();
        model.addAttribute("blogs", blogs);
        return "blog-list";
    }
}
