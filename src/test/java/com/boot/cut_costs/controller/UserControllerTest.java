package com.boot.cut_costs.controller;

import java.io.File;
import java.security.Principal;
import java.util.List;

import jersey.repackaged.com.google.common.collect.Lists;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.UserRepository;
import com.boot.cut_costs.utils.CommonUtils;
import com.boot.cut_costs.utils.UserTestingUtil;

public class UserControllerTest extends BaseControllerTest {

	private final static String DUMMY_NAME = "dummyname";
	private final static String DUMMY_DESCRIPTION = "dummydescription";
	private final static String DUMMY_IMAGE_ID = "123456789012345";
	private final static String DUMMY_USERNAME = "test@test.com";
	private final static String UPDATED_DUMMY_NAME = DUMMY_NAME + ".upda"; // ".updated" would make it too long
	private final static String UPDATED_DUMMY_DESCRIPTION = DUMMY_DESCRIPTION + ".updated";
	private final static String DUMMY_IMAGE = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAADklEQVQIW2NgQAXGZHAAGioAza6+Hk0AAAAASUVORK5CYII=";

	private final static String IMAGE_ID_FIELD_NAME = "imageId";
	private final static String DESCRIPTION_FIELD_NAME = "description";
	private final static String NAME_FIELD_NAME = "name";	
	private final static String IMAGE_FIELD_NAME = "image";
	
	private final static String USER_ENDPOINT_URL = "/user/";
	
	@Autowired
	private UserTestingUtil userTestingUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void testGet() throws Exception {
		Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(DUMMY_USERNAME);
		
		User user = userTestingUtil.createUser(DUMMY_USERNAME, DUMMY_NAME, DUMMY_DESCRIPTION, DUMMY_IMAGE_ID);

		List<User> users = Lists.newArrayList(userRepository.findAll());
		// this is not part of the rest controller test, just wanted to make sure that only one user was created
		Assert.assertEquals("wrong number of users created for a single user details", 1, users.size());

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				USER_ENDPOINT_URL + user.getId()).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		JSONObject content = new JSONObject(response.getContentAsString());

		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong name", DUMMY_NAME, content.get(NAME_FIELD_NAME));
		Assert.assertEquals("wrong description", DUMMY_DESCRIPTION, content.get(DESCRIPTION_FIELD_NAME));
		Assert.assertEquals("wrong image id", DUMMY_IMAGE_ID, content.get(IMAGE_ID_FIELD_NAME));			
	}

    @Test
	public void testGetCurrentUser() throws Exception {
		Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(DUMMY_USERNAME);

		userTestingUtil.createUser(DUMMY_USERNAME, DUMMY_NAME, DUMMY_DESCRIPTION, DUMMY_IMAGE_ID);
        
		RequestBuilder requestBuilder = MockMvcRequestBuilders
            .get(USER_ENDPOINT_URL)
            .principal(mockPrincipal)
            .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
		JSONObject content = new JSONObject(response.getContentAsString());
        Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong name", DUMMY_NAME, content.get(NAME_FIELD_NAME));
		Assert.assertEquals("wrong description", DUMMY_DESCRIPTION, content.get(DESCRIPTION_FIELD_NAME));
		Assert.assertEquals("wrong image id", DUMMY_IMAGE_ID, content.get(IMAGE_ID_FIELD_NAME));
    }
    
	@Test
    public void testUpdate() throws Exception {
    	Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(DUMMY_USERNAME);

		userTestingUtil.createUser(DUMMY_USERNAME, DUMMY_NAME, DUMMY_DESCRIPTION, DUMMY_IMAGE_ID);

        JSONObject jo = new JSONObject();
		jo.put(NAME_FIELD_NAME, UPDATED_DUMMY_NAME);
		jo.put(DESCRIPTION_FIELD_NAME, UPDATED_DUMMY_DESCRIPTION);
		jo.put(IMAGE_FIELD_NAME, DUMMY_IMAGE);
		String testUserJson = jo.toString();

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(USER_ENDPOINT_URL).content(testUserJson)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		String content = response.getContentAsString();

		List<User> createdUsers = Lists.newArrayList(userRepository.findAll());
		User createdUser = createdUsers.get(0);

		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong response content", "", content);
		Assert.assertEquals("number of users in the db should not change", 1, createdUsers.size());
		Assert.assertEquals("wrong updated user name", UPDATED_DUMMY_NAME, createdUser.getName());
		Assert.assertEquals("wrong updated user description", UPDATED_DUMMY_DESCRIPTION, createdUser.getDescription());
		File imageFile = CommonUtils.getImageFile(createdUser.getImageId());
		Assert.assertTrue("image file was not created", imageFile.exists());
    }
}