package com.boot.cut_costs.security.auth.jwt;

import com.boot.cut_costs.security.model.CustomUserDetails;
import com.boot.cut_costs.security.service.TokenAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
	
    private static Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);
    
	public JWTLoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url, HttpMethod.POST.toString()));
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		CustomUserDetails creds = new ObjectMapper().readValue(request.getInputStream(), CustomUserDetails.class);
        logger.debug("Authenticating login attemp from user " + creds.getUsername());
		return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(),creds.getPassword()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
		logger.debug("User " + auth.getName() + " successfully logged in");
		TokenAuthenticationService.addAuthentication(response, auth.getName());
		chain.doFilter(request, response);
	}
}