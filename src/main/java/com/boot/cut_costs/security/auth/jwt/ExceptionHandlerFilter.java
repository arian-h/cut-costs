package com.boot.cut_costs.security.auth.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.boot.cut_costs.exception.ExceptionHandlingAdvice;

/**
 * A custom filter which captures the RuntimeException's thrown by the other filters added after it
 */
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
	
	@Autowired
	private ExceptionHandlingAdvice exceptionHandlingAdvice;
    
	@Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
        	exceptionHandlingAdvice.RuntimeExceptionHandler(e);
        }
    }
}