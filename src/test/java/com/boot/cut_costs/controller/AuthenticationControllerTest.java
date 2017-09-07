package com.boot.cut_costs.controller;

import java.util.List;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.boot.cut_costs.model.CustomUserDetails;
import com.boot.cut_costs.model.User;
import com.google.common.collect.Lists;

public class AuthenticationControllerTest extends BaseControllerTest {
	
	public boolean isAuthenticationTest() {
		return true;
	}

	@Test
    public void testCreate() throws Exception {
		//prepare
		Authentication authentication = Mockito.mock(Authentication.class);
		String username = createUniqueEmail();
		Mockito.when(authentication.getName()).thenReturn(username);
		Mockito.when(
				authenticationManager.authenticate(Mockito
						.any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);
		String user_name = createUniqueAlphanumericString(10);
		JSONObject jo = new JSONObject();
		jo.put(USERNAME_FIELD_NAME, username);
		jo.put(NAME_FIELD_NAME, user_name);
		jo.put(PASSWORD_FIELD_NAME, DUMMY_PASSWORD);

		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(SIGNUP_ENDPOINT).content(jo.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		List<User> users = Lists.newArrayList(userRepository.findAll());
		List<CustomUserDetails> userDetails = Lists.newArrayList(userDetailsRepository.findAll());
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		String content = response.getContentAsString();
		
		//assert
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong response content", "", content);
		Assert.assertEquals("wrong number of users created in db", 1, users.size());
		Assert.assertEquals("user details created in db has wrong username", username, userDetails.get(0).getUsername());    		
		Assert.assertEquals("wrong user created in db", user_name, users.get(0).getName());
		Assert.assertEquals("wrong number of user details created in db", 1, userDetails.size());
    }
}
