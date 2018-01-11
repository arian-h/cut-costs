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
import com.boot.cut_costs.security.auth.jwt.ExceptionHandlerFilter;
import com.boot.cut_costs.security.auth.jwt.JWTAuthenticationFilter;

@EnableWebSecurity
@Configuration
@Import(AppConfig.class)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		
	@Autowired
	private JWTAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	private ExceptionHandlerFilter exceptionHandlerFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			//disable CSRF, so we can use postman
			//TODO likely to re-enable it after dev is done
			.csrf().disable()
			// we must specify ordering for our custom filter, otherwise it doesn't work
			.addFilterAfter(exceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterAfter(jwtAuthenticationFilter, ExceptionHandlerFilter.class)
			//as we are using JWT, we don't need Session. Sessions are harder to scale and manage
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
    	//TODO Change it back to the following after front-end active dev work is done
        web.ignoring()
        	.antMatchers("/api/auth/**");
    		/*
    		 * This is used for initializing h2 console. Remove this when going to prod
        		.antMatchers("/console/**"); 
        	*/
        web.ignoring()
        	.antMatchers(HttpMethod.OPTIONS, "/**");
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