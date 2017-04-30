package com.boot.cut_costs.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;

class TokenAuthenticationService {
	static final long EXPIRATIONTIME = 864_000_000; // 10 days
	static final String SECRET = "ThisIsASecret";
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";

	static void addAuthentication(HttpServletResponse res, String username) {
		String JWT = Jwts
				.builder()
				.setSubject(username)
				.setExpiration(
						new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		Cookie cookie = new Cookie("auth_code", JWT);
		cookie.setSecure(true);
		res.addCookie(cookie);
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}

	static Authentication getAuthentication(HttpServletRequest request, UserDetailsService customUserDetailsService) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			// parse the token.
			String userName = Jwts.parser().setSigningKey(SECRET)
					.parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getSubject();
			UserDetails user = customUserDetailsService.loadUserByUsername(userName);
			return user != null ? new UsernamePasswordAuthenticationToken(user.getUsername(),
					user.getPassword(), user.getAuthorities()) : null;
		}
		return null;
	}
}