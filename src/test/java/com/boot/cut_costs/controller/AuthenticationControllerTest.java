package com.boot.cut_costs.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.Filter;

import jersey.repackaged.com.google.common.collect.Lists;

import org.h2.tools.Server;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.UserDetailsRepository;
import com.boot.cut_costs.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerTest {
	
	@Autowired 
	private MockMvc mockMvc;
	
	@MockBean
	private AuthenticationManager authenticationManager;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private Filter springSecurityFilterChain;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	private final static String DUMMY_STRING = "whocares";
	
	@Before
	public void setup() throws SQLException {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.addFilters(springSecurityFilterChain).build();
		/*
		 * TODO: Delete the following two lines when going into production. These two lines help developer to 
		 * access the H2 web console through http://localhost:8082/
		 * */
	    Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
	    webServer.start();
	}

    @Test
    public void testCreate() throws Exception {
		String name = "Salam12345";
		String username = "test@test1.com";
		String password = "Hello1234567";
		
    	Authentication authentication = Mockito.mock(Authentication.class);
    	Mockito.when(authentication.getName()).thenReturn(DUMMY_STRING);
		Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);
		
		JSONObject jo = new JSONObject();
		
		jo.put("name", name);
		jo.put("username", username);
		jo.put("password", password);
		
		String exampleUserInfo = jo.toString();
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/signup")
				.content(exampleUserInfo)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		ArrayList<User> users = Lists.newArrayList(userRepository.findAll());
		ArrayList<UserDetails> userDetails = Lists.newArrayList(userDetailsRepository.findAll());
		
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		String content = response.getContentAsString();
		
		Assert.assertEquals("response status is wrong", 200, status);
		Assert.assertEquals("response content is wrong", "", content);
		Assert.assertEquals("wrong number of users created in db", 1, users.size());
		Assert.assertEquals("wrong user created in db", name, users.get(0).getName());
		Assert.assertEquals("wrong number of user details created in db", 1, userDetails.size());
		Assert.assertEquals("user details created in db has wrong username", username, userDetails.get(0).getUsername());
    }
    
}
