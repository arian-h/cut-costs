package com.boot.cut_costs.controller;

import java.sql.SQLException;

import javax.servlet.Filter;
import javax.transaction.Transactional;

import org.h2.tools.Server;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.boot.cut_costs.repository.UserDetailsRepository;
import com.boot.cut_costs.service.CustomUserDetailsService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public abstract class BaseControllerTest {
	
	@Autowired
	protected Filter springSecurityFilterChain;
	
	@Autowired 
	protected MockMvc mockMvc;
	
	@MockBean
	protected AuthenticationManager authenticationManager;

	@Autowired
	protected WebApplicationContext context;
	
	@Autowired
	protected CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@BeforeClass
	public static void init() throws SQLException {
		/*
		 * TODO: Delete the following two lines when going into production. These two lines help developer to 
		 * access the H2 web console through http://localhost:8082/
		 * */
	    Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
	    webServer.start();
	}
	
	@Before
	public void setUp() {
		if (isAuthenticationTest()) {
			mockMvc = MockMvcBuilders.webAppContextSetup(context)
					.addFilters(springSecurityFilterChain).build();
		} else {
			mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		}
	}

	public boolean isAuthenticationTest() {
		return false;
	}
}
