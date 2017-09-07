package com.boot.cut_costs.controller;

import java.security.Principal;
import java.util.List;

import jersey.repackaged.com.google.common.collect.Lists;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.boot.cut_costs.model.User;
import com.boot.cut_costs.utils.CommonUtils;

public class UserControllerTest extends BaseControllerTest {

	@Test
	public void testGet() throws Exception {
		//prepare
		Principal mockPrincipal = Mockito.mock(Principal.class);
		String username = createUniqueEmail();
        Mockito.when(mockPrincipal.getName()).thenReturn(username);
        String name = createUniqueAlphanumericString(10);
        String description = createUniqueAlphanumericString(40);
        String imageId = createUniqueAlphanumericString(15);
		User user = createUser(username, name, description, imageId);
		List<User> users = Lists.newArrayList(userRepository.findAll());

		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(USER_ENDPOINT_URL + user.getId())
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		JSONObject content = new JSONObject(response.getContentAsString());

		//assert
		Assert.assertEquals("wrong number of users created for a single user details", 1, users.size());
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong name", name, content.get(NAME_FIELD_NAME));
		Assert.assertEquals("wrong description", description, content.get(DESCRIPTION_FIELD_NAME));
		Assert.assertEquals("wrong image id", imageId, content.get(IMAGE_ID_FIELD_NAME));			
	}

    @Test
	public void testGetCurrentUser() throws Exception {
    	//prepare
    	Principal mockPrincipal = Mockito.mock(Principal.class);
		String username = createUniqueEmail();
        Mockito.when(mockPrincipal.getName()).thenReturn(username);
		String name = createUniqueAlphanumericString(10);
        String description = createUniqueAlphanumericString(40);
        String imageId = createUniqueAlphanumericString(15);
		createUser(username, name, description, imageId);
		
		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
            .get(USER_ENDPOINT_URL)
            .principal(mockPrincipal)
            .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
		JSONObject content = new JSONObject(response.getContentAsString());

		//assert
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong name", name, content.get(NAME_FIELD_NAME));
		Assert.assertEquals("wrong description", description, content.get(DESCRIPTION_FIELD_NAME));
		Assert.assertEquals("wrong image id", imageId, content.get(IMAGE_ID_FIELD_NAME));
    }

	@Test
    public void testUpdate() throws Exception {
		//prepare
    	Principal mockPrincipal = Mockito.mock(Principal.class);
		String username = createUniqueEmail();
        Mockito.when(mockPrincipal.getName()).thenReturn(username);
		String name = createUniqueAlphanumericString(10);
        String description = createUniqueAlphanumericString(40);
        String imageId = createUniqueAlphanumericString(15);
		createUser(username, name, description, imageId);
		String updatedName = createUniqueAlphanumericString(10);
        String updatedDescription = createUniqueAlphanumericString(40);
		JSONObject jo = new JSONObject();
        jo.put(NAME_FIELD_NAME, updatedName);
		jo.put(DESCRIPTION_FIELD_NAME, updatedDescription);
		jo.put(IMAGE_FIELD_NAME, DUMMY_IMAGE);

		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(USER_ENDPOINT_URL).content(jo.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		String content = response.getContentAsString();

		//assert
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong response content", "", content);
		List<User> createdUsers = Lists.newArrayList(userRepository.findAll());
		Assert.assertEquals("number of users in the db should not change", 1, createdUsers.size());
		User createdUser = createdUsers.get(0);
		Assert.assertEquals("wrong updated user name", updatedName, createdUser.getName());
		Assert.assertEquals("wrong updated user description", updatedDescription, createdUser.getDescription());
		Assert.assertTrue("image file was not created", CommonUtils.getImageFile(createdUser.getImageId()).exists());
    }
}