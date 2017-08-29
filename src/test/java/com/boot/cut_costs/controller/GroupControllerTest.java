package com.boot.cut_costs.controller;

import java.security.Principal;
import java.util.List;

import jersey.repackaged.com.google.common.collect.Lists;

import org.json.JSONArray;
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
	private final static String IMAGE_ID_FIELD_NAME = "imageId";
	
	@Autowired
	private UserTestingUtil userTestingUtil;
	
	@Autowired
	private GroupTestingUtil groupTestingUtil;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Test
	public void testCreateGroup() throws Exception {
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
		List<Group> groups = userTestingUtil.getOwnedGroups(user.getId());
		Assert.assertEquals("wrong number of groups created for the admin", 1, groups.size());
		Assert.assertEquals("wrong group is assigned to the admin", user.getOwnedGroups().get(0).getId(), createdGroup.getAdmin().getId());
		Assert.assertTrue("image file was not created", CommonUtils.getImageFile(createdGroup.getImageId()).exists());
	}
	
	@Test
	public void testUpdateGroup() throws Exception {
    	Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(DUMMY_USERNAME);

		User admin = userTestingUtil.createUser(DUMMY_USERNAME, DUMMY_USER_NAME, null, null);
		Group group = groupTestingUtil.createGroup(DUMMY_GROUP_NAME, DUMMY_GROUP_DESCRIPTION, DUMMY_IMAGE_ID, admin);

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

	@Test
	public void testGetGroup() throws Exception {
    	Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(DUMMY_USERNAME);

		User admin = userTestingUtil.createUser(DUMMY_USERNAME, DUMMY_USER_NAME, null, null);
		Group group = groupTestingUtil.createGroup(DUMMY_GROUP_NAME, DUMMY_GROUP_DESCRIPTION, DUMMY_IMAGE_ID, admin);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(GROUP_ENDPOINT_URL + group.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		JSONObject content = new JSONObject(response.getContentAsString());

		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong name", DUMMY_GROUP_NAME, content.get(NAME_FIELD_NAME));
		Assert.assertEquals("wrong description", DUMMY_GROUP_DESCRIPTION, content.get(DESCRIPTION_FIELD_NAME));
		Assert.assertEquals("wrong image id", DUMMY_IMAGE_ID, content.get(IMAGE_ID_FIELD_NAME));
	}
	
	/*
	 * Deleting a group shouldn't delete the members of that group
	 */
	@Test
	public void testDeleteGroupWithMembers() throws Exception {
    	Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(DUMMY_USERNAME);

		User admin = userTestingUtil.createUser(DUMMY_USERNAME, DUMMY_USER_NAME, null, null);
		Group group = groupTestingUtil.createGroup(DUMMY_GROUP_NAME, DUMMY_GROUP_DESCRIPTION, DUMMY_IMAGE_ID, admin);
		User member = userTestingUtil.createUser("test1@test.test", "testUser1" , null, null);
		group.addMember(member);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete(GROUP_ENDPOINT_URL + group.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		String content = response.getContentAsString();
		
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong response content", "", content);
		Assert.assertEquals("there should be no group left", 0, Lists.newArrayList(groupRepository.findAll()).size());
		Assert.assertEquals("wrong number of users exist", 2, Lists.newArrayList(userRepository.findAll()).size());
		Assert.assertTrue("admin shouldn't get deleted when deleting a group", userRepository.findById(admin.getId()) != null);
		Assert.assertTrue("group members shouldn't get deleted when deleting a group", userRepository.findById(member.getId()) != null);
		Assert.assertEquals("group should get deleted from user's list of member groups", 0, member.getMemberGroups().size());
		Assert.assertEquals("group should get deleted from user's list of owned groups", 0, admin.getMemberGroups().size());
	}

	@Test
	public void testGetAllGroups() throws Exception {
    	Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(DUMMY_USERNAME);

		User admin = userTestingUtil.createUser(DUMMY_USERNAME, DUMMY_USER_NAME, null, null);
		groupTestingUtil.createGroup(DUMMY_GROUP_NAME + "1", DUMMY_GROUP_DESCRIPTION + "1", DUMMY_IMAGE_ID, admin);
		groupTestingUtil.createGroup(DUMMY_GROUP_NAME + "2", DUMMY_GROUP_DESCRIPTION + "2", DUMMY_IMAGE_ID, admin);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(GROUP_ENDPOINT_URL)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		JSONArray content = new JSONArray(response.getContentAsString());
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong number of groups", 2, content.length());
		JSONObject responseGroup1 = (JSONObject)content.get(0);
		Assert.assertEquals("wrong name is returned for first group", DUMMY_GROUP_NAME + "1", (String)responseGroup1.get("name"));
		Assert.assertEquals("wrong description is returned for first group", DUMMY_GROUP_DESCRIPTION + "1", (String)responseGroup1.get("description"));
		JSONObject responseGroup2 = (JSONObject)content.get(1);
		Assert.assertEquals("wrong name is returned for second group", DUMMY_GROUP_NAME + "2", (String)responseGroup2.get("name"));
		Assert.assertEquals("wrong description is returned for second group", DUMMY_GROUP_DESCRIPTION + "2", (String)responseGroup2.get("description"));
	}

	@Test
	public void testDeleteGroupMember() throws Exception {
    	Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(DUMMY_USERNAME);

		User admin = userTestingUtil.createUser(DUMMY_USERNAME, DUMMY_USER_NAME, null, null);
		Group group = groupTestingUtil.createGroup(DUMMY_GROUP_NAME, DUMMY_GROUP_DESCRIPTION, null, admin);
		User member = userTestingUtil.createUser(DUMMY_USERNAME + "1", DUMMY_USER_NAME + "1", null, null);
		group.addMember(member);
		groupRepository.save(group);

		String testEndpointUrl = GROUP_ENDPOINT_URL + group.getId() + "/user/" + member.getId();
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete(testEndpointUrl)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();

		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertTrue("admin shouldn't get deleted when deleting a group", userRepository.findById(admin.getId()) != null);
		Assert.assertTrue("group shouldn't get deleted when deleting a member", groupRepository.findById(group.getId()) != null);
		Assert.assertTrue("member shouldn't get deleted", userRepository.findById(member.getId()) != null);
		List<User> groupMembers = group.getMembers();
		for (int index = 0; index < groupMembers.size(); index++) {
			Assert.assertNotEquals("group shouldn't reference the deleted member", member.getId(), groupMembers.get(index).getId());
		}
		List<Group> memberGroups = member.getMemberGroups();
		for (int index = 0; index < memberGroups.size(); index++) {
			Assert.assertNotEquals("user shouldn't reference the group it was deleted from", group.getId(), memberGroups.get(index).getId());
		}	
	}
	
	@Test
	public void testGetAllMembers() throws Exception {
    	Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(DUMMY_USERNAME);

		User admin = userTestingUtil.createUser(DUMMY_USERNAME, DUMMY_USER_NAME, null, null);
		Group group = groupTestingUtil.createGroup(DUMMY_GROUP_NAME, DUMMY_GROUP_DESCRIPTION, null, admin);
		User member1 = userTestingUtil.createUser(DUMMY_USERNAME + "1", DUMMY_USER_NAME + "1", null, null);
		User member2 = userTestingUtil.createUser(DUMMY_USERNAME + "2", DUMMY_USER_NAME + "2", null, null);
		group.addMember(member1);
		group.addMember(member2);
		groupRepository.save(group);

		String testEndpointUrl = GROUP_ENDPOINT_URL + group.getId() + "/user/";
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(testEndpointUrl)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();

		Assert.assertEquals("wrong response status", 200, status);
		JSONArray content = new JSONArray(response.getContentAsString());
		Assert.assertEquals("wrong number of members", 3, content.length()); // one user is the admin
		JSONObject responseMember1 = (JSONObject)content.get(0);
		Assert.assertEquals("wrong name is returned for the first member", DUMMY_USER_NAME + "1", (String)responseMember1.get("name"));
		JSONObject responseMember2 = (JSONObject)content.get(1);
		Assert.assertEquals("wrong name is returned for the second member", DUMMY_USER_NAME + "2", (String)responseMember2.get("name"));
		JSONObject responseMember3 = (JSONObject)content.get(2);
		Assert.assertEquals("wrong name is returned for the third member", DUMMY_USER_NAME, (String)responseMember3.get("name"));
	}

}
