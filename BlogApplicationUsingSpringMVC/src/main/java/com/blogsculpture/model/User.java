package com.blogsculpture.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Pattern.Flag;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@NotBlank(message = "name cannot be blank")
	@Size(min = 3, max = 20, message = "name must larger then 3 and lesser then 20 character.")
	private String name;

	@Email(regexp = "[a-z0-9.]+@[a-z0-9.]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Please provide a valid email")
	@NotBlank(message = "email cannot be blank")
	@Column(nullable = false, unique = true)
	private String email;

	private String nationality;
	
	private String mobileno;

	private String role;

	private String gender;

	private String religion;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Blog> blogs = new ArrayList<>();

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Address address;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Like> likes;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Comment> comments;

	public User(
			@NotBlank(message = "name cannot be blank") @Size(min = 3, max = 20, message = "name must larger then 3 and lesser then 20 character.") String name,
			@Email(regexp = "[a-z0-9.]+@[a-z0-9.]+\\.[a-z]{2,3}", flags = Flag.CASE_INSENSITIVE, message = "Please provide a valid email") @NotBlank(message = "email cannot be blank") String email,
			String nationality, String religion, LocalDateTime createdAt, String password, String gender,String mobileno) {
		super();
		this.name = name;
		this.email = email;
		this.nationality = nationality;
		this.religion = religion;
		this.createdAt = createdAt;
		this.password = password;
		this.gender = gender;
		this.mobileno = mobileno;
	}

}
