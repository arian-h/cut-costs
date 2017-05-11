package com.boot.cut_costs.security.endpoint;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.cut_costs.security.auth.jwt.JWTLoginFilter;
import com.boot.cut_costs.security.service.CustomUserDetailsService;

@RestController
public class UserAcccountController {
	
	private Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletResponse httpServletResponse) throws IOException {
		return "/home";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public void signup(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("SAlam");
	}
}
