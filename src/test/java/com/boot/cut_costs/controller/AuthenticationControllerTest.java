package com.boot.cut_costs.controller;

import java.util.List;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.boot.cut_costs.model.CustomUserDetails;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.UserDetailsRepository;
import com.boot.cut_costs.repository.UserRepository;
import com.google.common.collect.Lists;

public class AuthenticationControllerTest extends BaseControllerTest {

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	private final static String DUMMY_NAME = "testUser";
	private final static String DUMMY_PASSWORD = "Test123456";
	private final static String DUMMY_USERNAME = "test@test.com";

	private final static String NAME_FIELD_NAME = "name";
	private final static String USERNAME_FIELD_NAME = "username";
	private final static String PASSWORD_FIELD_NAME = "password";

	private final static String SIGNUP_ENDPOINT = "/signup";

	@Autowired
	private UserRepository userRepository;
	
	public boolean isAuthenticationTest() {
		return true;
	}

	@Test
    public void testCreate() throws Exception {
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(authentication.getName()).thenReturn(DUMMY_USERNAME);
		Mockito.when(
				authenticationManager.authenticate(Mockito
						.any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);

		JSONObject jo = new JSONObject();

		jo.put(NAME_FIELD_NAME, DUMMY_NAME);
		jo.put(USERNAME_FIELD_NAME, DUMMY_USERNAME);
		jo.put(PASSWORD_FIELD_NAME, DUMMY_PASSWORD);

		String testUserJson = jo.toString();

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(SIGNUP_ENDPOINT).content(testUserJson)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		List<User> users = Lists.newArrayList(userRepository.findAll());
		List<CustomUserDetails> userDetails = Lists.newArrayList(userDetailsRepository.findAll());

		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		String content = response.getContentAsString();

		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong response content", "", content);
		Assert.assertEquals("wrong number of users created in db", 1, users.size());
		Assert.assertEquals("wrong user created in db", DUMMY_NAME, users.get(0).getName());
		Assert.assertEquals("wrong number of user details created in db", 1, userDetails.size());
		Assert.assertEquals("user details created in db has wrong username", DUMMY_USERNAME, userDetails.get(0).getUsername());    		
    }
}
