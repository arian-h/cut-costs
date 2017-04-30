package com.boot.cut_costs.config.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.security.core.Authentication;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

public class JWTAuthenticationFilter extends GenericFilterBean {

	private UserDetailsService customUserDetailsService;
	
	public JWTAuthenticationFilter(UserDetailsService customUserDetailsService) {
		this.customUserDetailsService = customUserDetailsService;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		
		Authentication authentication = TokenAuthenticationService
				.getAuthentication((HttpServletRequest) request, customUserDetailsService);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		//go to the next filter
		filterChain.doFilter(request, response);
	}
}