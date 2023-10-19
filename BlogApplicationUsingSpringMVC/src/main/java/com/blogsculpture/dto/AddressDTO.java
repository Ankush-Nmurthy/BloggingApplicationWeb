package com.blogsculpture.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDTO {
	private String city;
	private String state;
	private String country;
}
