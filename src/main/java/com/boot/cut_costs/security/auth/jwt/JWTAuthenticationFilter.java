package com.boot.cut_costs.security.auth.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

import com.boot.cut_costs.service.AuthenticationService;

@Component
public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	@Autowired
	private UserDetailsService customUserDetailsService;
	
    private static Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    private final static UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();
    
    //Authentication will only be initiated for the request url matching this pattern
    final static String DEFAULT_FILTER_PROCESS_URL = "/api/**";
    
	public JWTAuthenticationFilter() {
		super(DEFAULT_FILTER_PROCESS_URL);
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(DEFAULT_FILTER_PROCESS_URL));
	}
	
	//we must set authentication manager for our custom filter, otherwise we would get an error
	@Override
	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
	    super.setAuthenticationManager(authenticationManager);
	}
	
	@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		return AuthenticationService.getAuthentication(request, customUserDetailsService);
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
	    logger.debug("failed authentication while attempting to access "+ URL_PATH_HELPER.getPathWithinApplication((HttpServletRequest) request));
	    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Authentication Failed");
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		// Changes/removes the currently authenticated principal
		SecurityContextHolder.getContext().setAuthentication(authResult);
	    chain.doFilter(request, response);
	}
	
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }
}