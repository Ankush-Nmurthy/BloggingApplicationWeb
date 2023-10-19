package com.blogsculpture.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSignupDTO {

	private String name;

	private String email;

	private String nationality;
	
	private String mobileno;

	@JsonIgnore
	private String role;

	private String religion;

	private String password;

	private String gender;

	private AddressDTO addressDTO;
}
