package com.blogsculpture.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Pattern.Flag;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString.Exclude;

@Entity
@Getter
@Setter
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

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] data;

	private String encoded;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private boolean isActive;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Exclude
	private List<Blog> blogs;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Exclude
	private Address address;

	// can remove both the likes and likes and comments because the user is
	// connected to blog, and both likes and comments are connected to blog
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Exclude
	private Set<Like> likes;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Exclude
	private Set<Comment> comments;

	public User(
			@NotBlank(message = "name cannot be blank") @Size(min = 3, max = 20, message = "name must larger then 3 and lesser then 20 character.") String name,
			@Email(regexp = "[a-z0-9.]+@[a-z0-9.]+\\.[a-z]{2,3}", flags = Flag.CASE_INSENSITIVE, message = "Please provide a valid email") @NotBlank(message = "email cannot be blank") String email,
			String nationality, String religion, LocalDateTime createdAt, String password, String gender,
			String mobileno) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + Objects.hash(createdAt, email, encoded, gender, isActive, mobileno, name, nationality,
				password, religion, role, updatedAt, userId);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(createdAt, other.createdAt) && Arrays.equals(data, other.data)
				&& Objects.equals(email, other.email) && Objects.equals(encoded, other.encoded)
				&& Objects.equals(gender, other.gender) && isActive == other.isActive
				&& Objects.equals(mobileno, other.mobileno) && Objects.equals(name, other.name)
				&& Objects.equals(nationality, other.nationality) && Objects.equals(password, other.password)
				&& Objects.equals(religion, other.religion) && Objects.equals(role, other.role)
				&& Objects.equals(updatedAt, other.updatedAt) && Objects.equals(userId, other.userId);
	}

}
