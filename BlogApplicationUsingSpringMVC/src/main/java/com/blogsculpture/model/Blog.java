package com.blogsculpture.model;

import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer blogId;

	@NotBlank(message = "titel cannot be blank.")
	private String title;
	private String imageAddress;
	private String category;
	private String content;
	private String accessType;
	private Date date = new Date();

	@ManyToOne(fetch = FetchType.LAZY)
	private User author;
}
