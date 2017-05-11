package com.boot.cut_costs.security.auth.jwt;

import com.boot.cut_costs.security.model.CustomUserDetails;
import com.boot.cut_costs.security.service.CustomUserDetailsService;
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

public class JWTSignupFilter extends AbstractAuthenticationProcessingFilter {

	private CustomUserDetailsService userDetailService;
	private static Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);

	public JWTSignupFilter(String url, AuthenticationManager authManager, CustomUserDetailsService userDetailService) {
		super(new AntPathRequestMatcher(url, HttpMethod.POST.toString()));
		this.userDetailService = userDetailService;
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		CustomUserDetails creds = new ObjectMapper().readValue(request.getInputStream(), CustomUserDetails.class);
		if (userDetailService.exists(creds.getUsername())) {
			logger.debug("Duplicate username " + creds.getUsername());
			throw new AuthenticationException("Duplicate username") {
				private static final long serialVersionUID = 1L;
			};
		}
		userDetailService.save(creds.getUsername(), creds.getPassword());
		return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(),creds.getPassword()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
		TokenAuthenticationService.addAuthentication(response, auth.getName());
		chain.doFilter(request, response);
	}
}