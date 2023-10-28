package com.blogsculpture.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexTempController {
	@GetMapping("/assets/index")
	public String getAssetsIndex() {
		return "/assets/index";
	}
	
	@GetMapping("/assets/form_component")
	public String getAssetsIndex2() {
		return "/assets/form_component";
	}
	
	@GetMapping("/assets/form_validation")
	public String getAssetsIndex3() {
		return "/assets/form_validation";
	}
	
	@GetMapping("/assets/general")
	public String getAssetsIndex4() {
		return "/assets/general";
	}
}
