package com.boot.cut_costs.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.boot.cut_costs.security.auth.jwt.JWTAuthenticationFilter;
import com.boot.cut_costs.security.auth.jwt.JWTLogoutHandler;
import com.boot.cut_costs.security.model.UserRepository;
import com.boot.cut_costs.security.service.CustomUserDetailsService;


@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired 
    private UserRepository accountRepository;
	@Autowired
	private JWTLogoutHandler logoutHandler;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/login").permitAll()
			.and()
			.authorizeRequests()
				.antMatchers("/signup").permitAll()
			.and()
			.authorizeRequests()
				.anyRequest().authenticated()
			.and()
				.logout().logoutUrl("/logout").logoutSuccessHandler(logoutHandler).logoutSuccessUrl("/login").invalidateHttpSession(true)
			.and()
			.addFilterBefore(
				new JWTAuthenticationFilter(userDetailsServiceBean()),
				UsernamePasswordAuthenticationFilter.class);
	}

	/*
	 * set user details services and password encoder
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceBean()).passwordEncoder(passwordEncoder());
	}

    @Override
    public CustomUserDetailsService userDetailsServiceBean() throws Exception {
        return customUserDetailsService;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}