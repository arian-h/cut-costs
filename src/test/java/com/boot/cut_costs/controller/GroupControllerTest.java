package com.boot.cut_costs.controller;

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

import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.GroupRepository;
import com.boot.cut_costs.repository.UserRepository;
import com.boot.cut_costs.utils.CommonUtils;
import com.boot.cut_costs.utils.GroupTestingUtil;
import com.boot.cut_costs.utils.UserTestingUtil;

public class GroupControllerTest extends BaseControllerTest {
	
	private final static String DUMMY_USERNAME = "test@test.com";
	private final static String DUMMY_USER_NAME = "testUser";

	private final static String DUMMY_GROUP_NAME = "dummygroupname";
	private final static String DUMMY_GROUP_DESCRIPTION = "dummygroupdescription";
	private final static String UPDATED_DUMMY_GROUP_DESC = DUMMY_GROUP_DESCRIPTION + ".upd";
	private final static String UPDATED_DUMMY_GROUP_NAME = DUMMY_GROUP_NAME + ".updated";

	private final static String DUMMY_IMAGE = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAADklEQVQIW2NgQAXGZHAAGioAza6+Hk0AAAAASUVORK5CYII=";
	private final static String DUMMY_IMAGE_ID = "123456789012345";

	private final static String GROUP_ENDPOINT_URL = "/group/";
	
	private final static String NAME_FIELD_NAME = "name";	
	private final static String DESCRIPTION_FIELD_NAME = "description";
	private final static String IMAGE_FIELD_NAME = "image";
	
	@Autowired
	private UserTestingUtil userTestingUtil;
	
	@Autowired
	private GroupTestingUtil groupTestingUtil;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Test
	public void testCreate() throws Exception {
    	Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(DUMMY_USERNAME);

		User user = userTestingUtil.createUser(DUMMY_USERNAME, DUMMY_USER_NAME, null, null);

        JSONObject jo = new JSONObject();
		jo.put(NAME_FIELD_NAME, DUMMY_GROUP_NAME);
		jo.put(DESCRIPTION_FIELD_NAME, DUMMY_GROUP_DESCRIPTION);
		jo.put(IMAGE_FIELD_NAME, DUMMY_IMAGE);
		String testGroupJson = jo.toString();

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(GROUP_ENDPOINT_URL).content(testGroupJson)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		String content = response.getContentAsString();

		List<Group> createdGroups = Lists.newArrayList(groupRepository.findAll());
		Group createdGroup = createdGroups.get(0);

		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong response content", "", content);
		Assert.assertEquals("wrong number of groups created", 1, createdGroups.size());
		Assert.assertEquals("wrong group name", DUMMY_GROUP_NAME, createdGroup.getName());
		Assert.assertEquals("wrong group description", DUMMY_GROUP_DESCRIPTION, createdGroup.getDescription());
		Assert.assertEquals("wrong admin is assigned to the group", user.getId(), createdGroup.getAdmin().getId());
		Assert.assertEquals("wrong number of groups created for the admin", 1, user.getOwnedGroups().size());
		Assert.assertEquals("wrong group is assigned to the admin", user.getOwnedGroups().get(0).getId(), createdGroup.getAdmin().getId());
		Assert.assertTrue("image file was not created", CommonUtils.getImageFile(createdGroup.getImageId()).exists());
	}
	
	@Test
	public void testUpdate() throws Exception {
    	Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(DUMMY_USERNAME);

		User user = userTestingUtil.createUser(DUMMY_USERNAME, DUMMY_USER_NAME, null, null);
		Group group = groupTestingUtil.createGroup(DUMMY_GROUP_NAME, DUMMY_GROUP_DESCRIPTION, DUMMY_IMAGE_ID, user);

        JSONObject jo = new JSONObject();
		jo.put(NAME_FIELD_NAME, UPDATED_DUMMY_GROUP_NAME);
		jo.put(DESCRIPTION_FIELD_NAME, UPDATED_DUMMY_GROUP_DESC);
		jo.put(IMAGE_FIELD_NAME, DUMMY_IMAGE);
		String testGroupJson = jo.toString();

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(GROUP_ENDPOINT_URL + group.getId()).content(testGroupJson)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();

		String content = response.getContentAsString();
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong response content", "", content);
		Assert.assertEquals("wrong number of groups exist", 1, Lists.newArrayList(groupRepository.findAll()).size());
		Group updatedGroup = groupRepository.findById(group.getId());
		Assert.assertEquals("wrong updated group name", UPDATED_DUMMY_GROUP_NAME, group.getName());
		Assert.assertEquals("wrong updated group description", UPDATED_DUMMY_GROUP_DESC, group.getDescription());
		Assert.assertTrue("image file was not created", CommonUtils.getImageFile(updatedGroup.getImageId()).exists());
		
	}
}
