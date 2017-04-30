package com.boot.cut_costs.config.security;

import com.boot.cut_costs.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JWTSignupFilter extends AbstractAuthenticationProcessingFilter {

	private AccountRepository accountRepository;
	
	private PasswordEncoder passwordEncoder;
	
	public JWTSignupFilter(String url, AuthenticationManager authManager,
			AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
		super(new AntPathRequestMatcher(url, "POST"));
		setAuthenticationManager(authManager);
		this.accountRepository = accountRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
			HttpServletResponse res) throws AuthenticationException,
			IOException, ServletException {

		CustomUserDetails creds = new ObjectMapper().readValue(
				req.getInputStream(), CustomUserDetails.class);

		if (accountRepository.findByUsername(creds.getUsername()) != null) {
			throw new AuthenticationException("Duplicate username") {
				private static final long serialVersionUID = 1L;
			};
		}
		
		CustomUserDetails userDetails = new CustomUserDetails(
				creds.getUsername(), passwordEncoder.encode(creds.getPassword()), true, true, true,
				true,
				AuthorityUtils.commaSeparatedStringToAuthorityList("USER_ROLE"));

		accountRepository.save(userDetails);

		return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(creds.getUsername(),
						creds.getPassword()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req,
			HttpServletResponse res, FilterChain chain, Authentication auth) {
		TokenAuthenticationService.addAuthentication(res, auth.getName());
	}
}