package com.boot.cut_costs.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.boot.cut_costs.AppConfig;
import com.boot.cut_costs.repository.UserDetailsRepository;
import com.boot.cut_costs.security.auth.jwt.JWTAuthenticationFilter;
import com.boot.cut_costs.service.CustomUserDetailsService;


@EnableWebSecurity
@Configuration
@Import(AppConfig.class)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired 
    private UserDetailsRepository accountRepository;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JWTAuthenticationFilter jwtAuthenticationFilter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
        	.csrf().disable()
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests()
	            .antMatchers("/**").authenticated().and()
			.sessionManagement()
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	/*
	 * Apparently, permitAll() doesn't work for custom filters, therefore we ignore the signup and login endpoints 
	 * here
	 */
    @Override
    public void configure(WebSecurity web)
            throws Exception {
        web.ignoring()
        	.antMatchers(HttpMethod.POST, "/auth/**");
    		/*
    		 * This is used for initializing h2 console. Remove this when going to prod
        		.antMatchers("/console/**"); 
        	*/
    }
	
	/*
	 * set user details services and password encoder
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceBean()).passwordEncoder(passwordEncoder());
	}
    
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
    /* Stopping spring from adding filter by default */
    @Bean
    public FilterRegistrationBean rolesAuthenticationFilterRegistrationDisable(JWTAuthenticationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }
    
}