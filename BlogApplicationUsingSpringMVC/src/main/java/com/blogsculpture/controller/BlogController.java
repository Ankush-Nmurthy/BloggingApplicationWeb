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

    @GetMapping("/blog")
    public String getBlogPageOfUser() {
        return "/commonblogLayout/blogIndex";
    }

    
}
