package com.blogsculpture.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private LocalDateTime commentedOn;

	private String comment;

	@ManyToOne
	@JoinColumn(name = "blog_id")
	private Blog blog;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		return Objects.equals(blog, other.blog) && Objects.equals(comment, other.comment)
				&& Objects.equals(id, other.id) && Objects.equals(user, other.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(blog, comment, id, user);
	}

}
