package com.blogsculpture.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "like_table")
public class Like {
	
	public enum Status{
		LIKED,DISLIKED
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime likeDate;
    private Status status;
    
    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
}
