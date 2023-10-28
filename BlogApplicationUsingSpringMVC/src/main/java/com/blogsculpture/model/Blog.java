package com.blogsculpture.model;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {

	public enum Status {
		DRAFT, COMPLETED
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer blogId;

	@NotBlank(message = "titel cannot be blank.")
	private String title;

	private String imageAddress;

	private String category;

	private String content;

	// private or public
	private String accessType;

	private Date date = new Date();

	//Drafted or completed.
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User author;

	@OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
	private Set<Like> likes;

	@OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
	private Set<Comment> comments;
	
}
