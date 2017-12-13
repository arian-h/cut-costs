package com.boot.cut_costs.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class AuthenticationService {

	private static final long EXPIRATIONTIME = 864_000_000; // 10 days
	private static final String SECRET = "ThisIsASecret";
	private static final String TOKEN_PREFIX = "Bearer";
	private static final String HEADER_STRING = "Authorization";
	
	public static void addAuthentication(HttpServletResponse res, String username) {
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + generateJWToken(username));
	}

	public static String generateJWToken(String username) {
		return Jwts
				.builder()
				.setSubject(username)
				.setExpiration(
						new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
	}

	public static Authentication getAuthentication(HttpServletRequest request, UserDetailsService customUserDetailsService) throws AuthenticationException {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			// parse the token
			String userName = Jwts.parser().setSigningKey(SECRET)
					.parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
					.getSubject();
			UserDetails user = customUserDetailsService.loadUserByUsername(userName);
			return user != null ? new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(),user.getAuthorities()) : null;
		}
		throw new BadCredentialsException("Bad credentials provided");
	}

}