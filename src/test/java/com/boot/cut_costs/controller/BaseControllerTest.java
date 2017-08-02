package com.boot.cut_costs.controller;

import java.sql.SQLException;

import javax.servlet.Filter;

import org.h2.tools.Server;
import org.junit.Before;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BaseControllerTest {
	
	@Autowired
	protected Filter springSecurityFilterChain;
	
	@Autowired 
	protected MockMvc mockMvc;
	
	@MockBean
	protected AuthenticationManager authenticationManager;

	@Autowired
	protected WebApplicationContext context;
	
	@Before
	public void setup(boolean setSecurityFilterChain) throws SQLException {
		if (setSecurityFilterChain) {
			mockMvc = MockMvcBuilders.webAppContextSetup(context)
					.addFilters(springSecurityFilterChain).build();			
		} else {
			mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		}
		/*
		 * TODO: Delete the following two lines when going into production. These two lines help developer to 
		 * access the H2 web console through http://localhost:8082/
		 * */
	    Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
	    webServer.start();
	}

}
