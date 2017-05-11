package com.boot.cut_costs.security.auth.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.security.core.Authentication;

import com.boot.cut_costs.security.service.TokenAuthenticationService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

public class JWTAuthenticationFilter extends GenericFilterBean {
	
	private UserDetailsService customUserDetailsService;
    private static Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    private final static UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	public JWTAuthenticationFilter(UserDetailsService customUserDetailsService) {
		this.customUserDetailsService = customUserDetailsService;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		Authentication authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest) request, customUserDetailsService);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		if (authentication == null) {
			logger.debug("failed authentication while attempting to access " + urlPathHelper.getPathWithinApplication((HttpServletRequest) request));
		}
		filterChain.doFilter(request, response);
	}
}