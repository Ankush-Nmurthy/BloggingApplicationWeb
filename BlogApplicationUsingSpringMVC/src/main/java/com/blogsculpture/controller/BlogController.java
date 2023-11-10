package com.blogsculpture.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.blogsculpture.appconfig.CustomUser;
import com.blogsculpture.model.Blog;
import com.blogsculpture.model.Blog.AccessType;
import com.blogsculpture.model.Blog.Status;
import com.blogsculpture.model.User;
import com.blogsculpture.service.BlogService;
import com.blogsculpture.service.UserService;
import com.blogsculpture.exceptions.NotFoundException;

@Controller
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String getIndexPageOfWebApplication() {
        return "/index";
    }

    // for this end point there is no need to get authenticated and ie (no login
    // needed).
    @GetMapping("/blogsculpture")
    public String homeBlogPageHandler() {
        System.out.println("Inside the local maping.");
        return "redirect:/blog/0?category=all";
    }

    @GetMapping("/blog/redirectUser")
    public String redirectUserToAccountFromBlogPage(Authentication authentication) {
        // System.out.println("inside blog/redirestUser class");
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.stream().anyMatch((auth) -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin";
            } else if (authorities.stream().anyMatch((auth) -> auth.getAuthority().equals("ROLE_USER"))) {
                return "redirect:/user";
            }
        }
        return "redirect:/blog";
    }

    @GetMapping("/addblog")
    public String createNewBlog(Model model) {
        model.addAttribute("blogEntity", new Blog());
        return "commonblogLayout/add_blog";
    }

    @PostMapping("/addblog")
    public String createNewBlog(@RequestParam("file") MultipartFile file, @ModelAttribute("blogEntity") Blog blogEntity,
            @RequestParam("content") String content) {
        CustomUser loggedInUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = loggedInUser.getUser();
        try {
            if (!file.isEmpty()) {
                blogEntity.setData(file.getBytes());
                blogEntity.setContent(content);
                blogEntity.setEncoded(Base64.getEncoder().encodeToString(blogEntity.getData()));
                blogEntity.setAuthor(user);
                user.getBlogs().add(blogEntity);
                blogService.addBlog(blogEntity);
                userService.saveUser(user);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/addblog?success=true";
    }

    @GetMapping("/blogview/{id}")
    public String viewBlogByBlogId(Model model, @PathVariable Integer id) {
        Blog blog = blogService.getBlog(id);
        blog.setEncoded(Base64.getEncoder().encodeToString(blog.getData()));
        model.addAttribute("blogDto", blog);
        model.addAttribute("author", blog.getAuthor().getName().toUpperCase());
        model.addAttribute("likeCount", blogService.getLikesForABlog(id));
        return "commonblogLayout/view_blog";
    }

    @GetMapping("/editable/{id}")
    public String editBlogByBlogId(Model model, @PathVariable Integer id) {
        Blog blog = blogService.getBlog(id);
        blog.setEncoded(Base64.getEncoder().encodeToString(blog.getData()));
        model.addAttribute("blogEntity", blog);

        return "commonblogLayout/edit_blog";
    }

    @PostMapping("/editBlog/{id}")
    public String editBlogByBlogId(@RequestParam("file") MultipartFile file,
            @ModelAttribute("blogEntity") Blog blogEntity, @RequestParam("content") String content,
            @PathVariable Integer id) {
        Blog blogEntity2 = blogService.getBlog(id);
        CustomUser loggedInUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = loggedInUser.getUser();
        if (blogEntity2.getAuthor().getUserId() != user.getUserId()) {
            return "redirect:/editable/" + id + "?error=You are not the author of this blog to edit";
        }
        try {
            if (!file.isEmpty()) {
                blogEntity2.setData(file.getBytes());
                blogEntity2.setEncoded(Base64.getEncoder().encodeToString(blogEntity2.getData()));
            }
            blogEntity2.setAccessType(blogEntity.getAccessType());
            blogEntity2.setCategory(blogEntity.getCategory());
            blogEntity2.setStatus(blogEntity.getStatus());
            blogEntity2.setContent(content);
            blogEntity2.setUpdated(new Date());
            blogEntity2.setTitle(blogEntity.getTitle());
            blogService.addBlog(blogEntity2);
        } catch (IOException e) {
            return "error";
        }
        return "redirect:/editable/" + id + "?success = blog edited successfully";
    }

    // This end point is used by two pages
    // 1. blog_page(which is the user blog-page)
    // 2. blogIndex(which can accessed without login);
    @GetMapping("/blog/{currentPage}")
    public String getCategory(@RequestParam("category") String category, @PathVariable Integer currentPage,
            Model model) {
        System.out.println("inside get category method");
        if (currentPage == null || currentPage <= 0) {
            currentPage = 1;
        }
        Pageable pageable = PageRequest.of(currentPage - 1, 6);
        Page<Blog> page = category.equals("all") ? blogService.findAllBlogs(pageable)
                : blogService.getBlogsByCategory(category, currentPage - 1);
        if (page.isEmpty())
            throw new NotFoundException("Content for this page is empty!");

        List<Blog> perPageBlogs = page.getContent().stream()
                .peek(e -> e.setEncoded(Base64.getEncoder().encodeToString(e.getData()))).collect(Collectors.toList());

        // System.out.println(perPageBlogs.size());

        model.addAttribute("blogDto", perPageBlogs);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentcategory", category);

        String User = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            return "commonblogLayout/blog_page";
        }
        return "commonblogLayout/blogIndex";
    }

    @GetMapping("/blog/trending/{limit}")
    @ResponseBody
    public List<Blog> findTrendingBlogs(@PathVariable Integer limit) {
        return blogService.findTopTrendingBlog(limit);
    }

    // http://localhost:9090/blogservices/1?category=all&accesstype=null&status=null
    @GetMapping("/blogservices/{currentpage}")
    public String getBlogServicesPage(Model model, @PathVariable("currentpage") Integer currentPage,
            @RequestParam(required = false) String category, @RequestParam(required = false) String accesstype,
            @RequestParam(required = false) String status) {

        // using the current logged in user in-order to get his
        // blog's and to display on his user dash-board.
        CustomUser loggedInUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = loggedInUser.getUser();
        userService.setUsernameAndProfileImageToModel(model);

        AccessType accessTypeEnum = accesstype.equals("null") ? AccessType.UNKNOWN : AccessType.valueOf(accesstype);
        Status statusEnum = status.equals("null") ? Status.UNKNOWN : Status.valueOf(status);

        Page<Blog> page = null;
        if (currentPage == null || currentPage <= 0) {
            currentPage = 1;
        }

        // TODO ==> test and expected = 1st method;

        // debugging;
        // System.out.println("accessTypeEnum : " + accessTypeEnum + " statusEnum : " +
        // statusEnum + " Category : "
        // + category + " currentPage : " + currentPage);

        if ((category.equals("all") || (category.equals("null")))
                && (accessTypeEnum == AccessType.UNKNOWN && statusEnum == Status.UNKNOWN)) {
            Pageable pageable = PageRequest.of(currentPage - 1, 3);
            // System.out.println("inside : findByAuthorUserIdOrderByDateDesc method-1");
            page = blogService.findByAuthorUserIdOrderByDateDesc(user.getUserId(), pageable);
        } else {
            if (!category.equals("null")) {
                if (accessTypeEnum != AccessType.UNKNOWN && statusEnum != Status.UNKNOWN) {
                    // System.out.println("inside :
                    // findByCategoryAndAccessTypeAndStatusAndAuthorUserIdOrderByDateDesc
                    // method-2");
                    page = blogService.findByCategoryAndAccessTypeAndStatusAndAuthorUserIdOrderByDateDesc(category,
                            accessTypeEnum, statusEnum, user.getUserId(), currentPage);
                } else if (accessTypeEnum != AccessType.UNKNOWN) {
                    // System.out.println("inside :
                    // findByCategoryAndAccessTypeAndAuthorUserIdOrderByDateDesc method-3");
                    page = blogService.findByCategoryAndAccessTypeAndAuthorUserIdOrderByDateDesc(category,
                            accessTypeEnum, user.getUserId(), currentPage);
                } else if (statusEnum != Status.UNKNOWN) {
                    // System.out.println("inside :
                    // findByCategoryAndStatusAndAuthorUserIdOrderByDateDesc method-4");
                    page = blogService.findByCategoryAndStatusAndAuthorUserIdOrderByDateDesc(category, statusEnum,
                            user.getUserId(), currentPage);
                } else {
                    // System.out.println("inside : findByCategoryAndAuthorUserIdOrderByDateDesc
                    // method-5");
                    page = blogService.findByCategoryAndAuthorUserIdOrderByDateDesc(category, user.getUserId(),
                            currentPage);
                }
            }
        }

        if (page.isEmpty()) {
            throw new NotFoundException("Content for this page is empty!");
        }

        List<Blog> perPageBlogs = page.getContent().stream()
                .peek(e -> e.setEncoded(Base64.getEncoder().encodeToString(e.getData()))).collect(Collectors.toList());

        // System.out.println(perPageBlogs.size());

        model.addAttribute("blogDto", perPageBlogs);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentcategory", category);
        model.addAttribute("currentaccesstype", accesstype);
        model.addAttribute("currentstatus", status);
        if (loggedInUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return "admin/blogservices";
        }
        return "user/blogservices";
    }

}
