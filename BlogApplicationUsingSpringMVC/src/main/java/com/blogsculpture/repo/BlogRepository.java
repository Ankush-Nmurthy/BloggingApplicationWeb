package com.blogsculpture.repo;

import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.blogsculpture.model.Blog;
import com.blogsculpture.model.Like;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
	List<Blog> findByAuthorUserId(Integer userId);

	List<Blog> findByAuthorName(String name);

	@Query("SELECT b.likes from Blog b WHERE b.blogId = :id")
	Set<Like> findAllLikesByBlogId(Integer id);

	@Query("SELECT b FROM Blog b ORDER BY SIZE(b.likes) DESC")
	List<Blog> findTopTrendingBlog(Pageable pageable);

//	This method is used in view-page[blog_page.html]
	Page<Blog> findByCategoryOrderByDateDesc(String category, Pageable pageable);

	// TODO ==> should be corrected again.
//	@Query("SELECT b FROM Blog b INNER JOIN User u ON b.author.userId = u.userId WHERE"
//			+ "(:category IS NULL OR b.category = :category) AND "
//			+ "(:access IS NULL OR b.accessType = :access) AND "
//			+ "(:status IS NULL OR b.status = :status) AND "
//			+ "u.userId = :userId")
//	Page<Blog> findLoggedInUsersPrivateOrPublicBlogsAndCompletedOrDraftedBlogs(@Param("access") AccessType access,
//			@Param("status") Status status, @Param("category") String category, @Param("userId") Integer userId,
//			Pageable page);
//	
//	@Query("SELECT b FROM Blog b INNER JOIN User u ON b.author.userId = u.userId WHERE b.category = :category AND "
//			+ "b.accessType = :access AND " + "b.status = :status AND " + "u.userId = :userId")
//	Page<Blog> findLoggedInUsersPrivateOrPublicBlogsAndCompletedOrDraftedBlogs(@Param("access") AccessType access,
//			@Param("status") Status status, @Param("category") String category, @Param("userId") Integer userId,
//			Pageable page);

	//1. findAllBlogs of user who logged in to the account.
	Page<Blog> findByAuthorUserIdOrderByDateDesc(Integer authorUserId, Pageable pageable);

	// 2. only category;
	Page<Blog> findByCategoryAndAuthorUserIdOrderByDateDesc(String category, Integer authorUserId, Pageable pageable);
	
	// 3. category and access-type;
	Page<Blog> findByCategoryAndAccessTypeAndAuthorUserIdOrderByDateDesc(String category, Blog.AccessType status,
			Integer authorUserId, Pageable pageable);

	// 4, category and status;
	Page<Blog> findByCategoryAndStatusAndAuthorUserIdOrderByDateDesc(String category, Blog.Status status,
			Integer authorUserId, Pageable pageable);

	// 5. category, status, access-type, AuthorId and pageable.
	Page<Blog> findByCategoryAndAccessTypeAndStatusAndAuthorUserIdOrderByDateDesc(String category,
			Blog.AccessType accessType, Blog.Status status, Integer authorUserId, Pageable pageable);
	
	
}
