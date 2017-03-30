package com.boot.cut_costs.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestAPI {
	@RequestMapping("/")
	public String home() {
		return "hello";
	}
}
