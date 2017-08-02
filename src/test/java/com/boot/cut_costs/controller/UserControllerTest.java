package com.boot.cut_costs.controller;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.boot.cut_costs.utils.UserTestingUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

	private final static String DUMMY_NAME = "dummyname";
	private final static String DUMMY_DESCRIPTION = "dummyusername";
	private final static String DUMMY_IMAGE_ID = "123456789012345";
	private final static String USER_ENDPOINT_PREFIX = "/user/";
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private UserTestingUtils userTestingUtil;

	@Before
	public void setup() throws SQLException {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		/*
		 * TODO: Delete the following two lines when going into production.
		 * These two lines help developer to access the H2 web console through
		 * http://localhost:8082/
		 */
		Server webServer = Server.createWebServer("-web", "-webAllowOthers",
				"-webPort", "8082");
		webServer.start();
	}

	@Test
	public void testGet() throws Exception {
		long userId = userTestingUtil.createUser(DUMMY_NAME, DUMMY_DESCRIPTION,
				DUMMY_IMAGE_ID);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				USER_ENDPOINT_PREFIX + userId).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		JSONObject content = new JSONObject(response.getContentAsString());
		
		Assert.assertEquals("response status is wrong", 200, status);
		Assert.assertEquals("name is wrong", DUMMY_NAME, content.get("name"));
		Assert.assertEquals("description is wrong", DUMMY_DESCRIPTION, content.get("description"));
		Assert.assertEquals("image id is wrong", DUMMY_IMAGE_ID, content.get("imageId"));
	}

}
