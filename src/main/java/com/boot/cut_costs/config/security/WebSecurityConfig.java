package com.boot.cut_costs.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.boot.cut_costs.repository.AccountRepository;


@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired 
    private AccountRepository accountRepository;

	private PasswordEncoder passwordEncoder = passwordEncoder();
	
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
				.logout().logoutUrl("/logout").logoutSuccessUrl("/login").deleteCookies("auth_code").invalidateHttpSession(true)
			.and()
			// We filter the api/signup requests
			.addFilterBefore(
				new JWTSignupFilter("/signup", authenticationManager(),
						accountRepository, passwordEncoder),
				UsernamePasswordAuthenticationFilter.class)
			// We filter the api/login requests
			.addFilterBefore(
				new JWTLoginFilter("/login", authenticationManager()),
				UsernamePasswordAuthenticationFilter.class)
			// And filter other requests to check the presence of JWT in
			// header
			.addFilterBefore(new JWTAuthenticationFilter(userDetailsServiceBean()),
				UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailsServiceBean()).passwordEncoder(passwordEncoder);
	}

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new CustomUserDetailsService(accountRepository);
    }
    
    public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}