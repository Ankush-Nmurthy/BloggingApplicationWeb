package com.blogsculpture.model;

import java.util.Date;
import java.util.Set;
import jakarta.persistence.Transient;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString.Exclude;

@Entity
// @Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Blog {

	public enum Status {
		DRAFT, COMPLETED, UNKNOWN
	}

	public enum AccessType {
		PUBLIC, PRIVATE, UNKNOWN
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer blogId;

	@NotBlank(message = "title cannot be blank.")
	private String title;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	@Exclude
	private byte[] data;

	@Transient
	@Exclude
	private String encoded;

	private String category;

	private String content;

	// private or public
	private AccessType accessType;

	private Date date = new Date();

	private Date updated;

	// Drafted or completed.
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@Exclude
	private User author;

	@OneToMany(mappedBy = "blog", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Exclude
	private Set<Like> likes;

	@OneToMany(mappedBy = "blog", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Exclude
	private Set<Comment> comments;

}
