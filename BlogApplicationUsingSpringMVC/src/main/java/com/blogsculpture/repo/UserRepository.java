package com.blogsculpture.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.blogsculpture.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByEmail(String email);

	Integer countByIsActive(boolean isActive);

	@Query("SELECT COUNT(u) FROM User u where u.role = 'ROLE_ADMIN'")
	Integer administratorsOfApplication();
	
	@Query(" select Month(createdAt) as month,YEAR(createdAt) as year,count(u) from User u group by month,year")
	List<Object[]> graphForUserRegistrationMonthWise();
}
