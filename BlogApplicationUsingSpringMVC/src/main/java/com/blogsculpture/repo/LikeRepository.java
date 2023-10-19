package com.blogsculpture.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.blogsculpture.model.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer>{

}
