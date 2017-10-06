package com.boot.cut_costs.controller;

import java.security.Principal;
import java.util.List;

import jersey.repackaged.com.google.common.collect.Lists;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.utils.CommonUtils;

public class GroupControllerTest extends BaseControllerTest {

	@Test
	public void testCreate() throws Exception {
		//prepare
		Principal mockPrincipal = Mockito.mock(Principal.class);
		String username = createUniqueEmail();
        Mockito.when(mockPrincipal.getName()).thenReturn(username);
        String user_name = createUniqueAlphanumericString(10);
		User user = createUser(username, user_name, null, null);
        JSONObject jo = new JSONObject();
        String group_name = createUniqueAlphanumericString(10);
        String group_description = createUniqueAlphanumericString(50);
		jo.put(NAME_FIELD_NAME, group_name);
		jo.put(DESCRIPTION_FIELD_NAME, group_description);
		jo.put(IMAGE_FIELD_NAME, DUMMY_IMAGE);
		
		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(GROUP_ENDPOINT_URL).content(jo.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();

		//assert
		List<Group> createdGroups = Lists.newArrayList(groupRepository.findAll());
		Group createdGroup = createdGroups.get(0);
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong number of groups created", 1, createdGroups.size());
		Assert.assertEquals("wrong group name", group_name, createdGroup.getName());
		Assert.assertEquals("wrong group description", group_description, createdGroup.getDescription());
		Assert.assertEquals("wrong admin is assigned to the group", user.getId(), createdGroup.getAdmin().getId());
		List<Group> groups = userRepository.findById(user.getId()).getOwnedGroups();
		Assert.assertEquals("wrong number of groups created for the admin", 1, groups.size());
		Assert.assertEquals("wrong group is assigned to the admin", user.getOwnedGroups().get(0).getId(), createdGroup.getId());
		Assert.assertTrue("image file not created", CommonUtils.getImageFile(createdGroup.getImageId()).exists());
	}
	
	@Test
	public void testUpdate() throws Exception {
    	//prepare
		Principal mockPrincipal = Mockito.mock(Principal.class);
		String username = createUniqueEmail();
        Mockito.when(mockPrincipal.getName()).thenReturn(username);
        String user_name = createUniqueAlphanumericString(10);
        String group_name = createUniqueAlphanumericString(10);
        String group_description = createUniqueAlphanumericString(50);
		User admin = createUser(username, user_name, null, null);
		Group group = createGroup(group_name, group_description, DUMMY_IMAGE_ID, admin);
        JSONObject jo = new JSONObject();
        String updated_group_name = createUniqueAlphanumericString(10);
        String updated_group_description = createUniqueAlphanumericString(50);
        jo.put(NAME_FIELD_NAME, updated_group_name);
		jo.put(DESCRIPTION_FIELD_NAME, updated_group_description);
		jo.put(IMAGE_FIELD_NAME, DUMMY_IMAGE);

		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(GROUP_ENDPOINT_URL + group.getId()).content(jo.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		
		//assert
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong number of groups exist", 1, Lists.newArrayList(groupRepository.findAll()).size());
		Group updatedGroup = groupRepository.findById(group.getId());
		Assert.assertEquals("wrong updated group name", updated_group_name, group.getName());
		Assert.assertEquals("wrong updated group description", updated_group_description, group.getDescription());
		Assert.assertTrue("image file not created", CommonUtils.getImageFile(updatedGroup.getImageId()).exists());
	}

	@Test
	public void testGet() throws Exception {
		//prepare
    	Principal mockPrincipal = Mockito.mock(Principal.class);
		String username = createUniqueEmail();
        Mockito.when(mockPrincipal.getName()).thenReturn(username);
        String user_name = createUniqueAlphanumericString(10);
        String group_name = createUniqueAlphanumericString(10);
        String group_description = createUniqueAlphanumericString(50);
		User admin = createUser(username, user_name, null, null);
		Group group = createGroup(group_name, group_description, DUMMY_IMAGE_ID, admin);

		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(GROUP_ENDPOINT_URL + group.getId())
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		JSONObject content = new JSONObject(response.getContentAsString());

		//assert
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong name", group_name, content.get(NAME_FIELD_NAME));
		Assert.assertEquals("wrong description", group_description, content.get(DESCRIPTION_FIELD_NAME));
		Assert.assertEquals("wrong image id", DUMMY_IMAGE_ID, content.get(IMAGE_ID_FIELD_NAME));
	}
	
	/*
	 * Deleting a group shouldn't delete the members of that group
	 */
	@Test
	public void testDeleteGroupWithMembers() throws Exception {
		//prepare
    	Principal mockPrincipal = Mockito.mock(Principal.class);
    	String[] usernames = createUniqueEmail(3);
    	String[] user_names = createUniqueAlphanumericString(10, 3);
    	Mockito.when(mockPrincipal.getName()).thenReturn(usernames[0]);
        User[] users = new User[2];
        for (int index = 0; index < 2; index++) {
        	users[index] = createUser(usernames[index], user_names[index], null, null);
        }
        String group_name = createUniqueAlphanumericString(10);
        String group_description = createUniqueAlphanumericString(50);
		Group group = createGroup(group_name, group_description, DUMMY_IMAGE_ID, users[0]);
		group.addMember(users[1]);

		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete(GROUP_ENDPOINT_URL + group.getId())
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		String content = response.getContentAsString();
		
		//assert
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong response content", "", content);
		Assert.assertEquals("there should be no group left", 0, Lists.newArrayList(groupRepository.findAll()).size());
		Assert.assertEquals("wrong number of users exist", 2, Lists.newArrayList(userRepository.findAll()).size());
		Assert.assertTrue("admin shouldn't get deleted when deleting a group", userRepository.findById(users[0].getId()) != null);
		Assert.assertTrue("group members shouldn't get deleted when deleting a group", userRepository.findById(users[1].getId()) != null);
		Assert.assertEquals("group should get deleted from user's list of member groups", 0, users[1].getMemberGroups().size());
		Assert.assertEquals("group should get deleted from user's list of owned groups", 0, users[0].getMemberGroups().size());
	}

	/*
	 * Get a list of all groups a user is a member of
	 */
	@Test
	public void testListGroups() throws Exception {
    	//prepare
		Principal mockPrincipal = Mockito.mock(Principal.class);
		String username = createUniqueEmail();
        String user_name = createUniqueAlphanumericString(10);
        Mockito.when(mockPrincipal.getName()).thenReturn(username);
		User admin = createUser(username, user_name, null, null);
		String[] group_names = createUniqueAlphanumericString(10, 2);
		String[] group_descriptions = createUniqueAlphanumericString(50, 2);
		for (int index = 0; index < 2; index++) {
			createGroup(group_names[index], group_descriptions[index], DUMMY_IMAGE_ID, admin);
		}

		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(GROUP_ENDPOINT_URL)
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		JSONArray content = new JSONArray(response.getContentAsString());
		
		//assert
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong number of groups", 2, content.length());
		for (int index = 0; index < 2; index++) {
			JSONObject resGroup = (JSONObject)content.get(index);
			Assert.assertEquals("wrong name is returned for group" + index, group_names[index], (String)resGroup.get("name"));
			Assert.assertEquals("wrong description is returned for group" + index, group_descriptions[index], (String)resGroup.get("description"));
		}
	}

	@Test
	public void testRemoveMember() throws Exception {
		//prepare
    	Principal mockPrincipal = Mockito.mock(Principal.class);
        User[] users = new User[2];
        String[] usernames = createUniqueEmail(2);
        for (int index = 0; index < 2; index++) {
        	users[index] = createUser(usernames[index], createUniqueAlphanumericString(10), null, null);
        }
        Mockito.when(mockPrincipal.getName()).thenReturn(usernames[0]);
        Group group = createGroup(createUniqueAlphanumericString(10), createUniqueAlphanumericString(50), null, users[0]);
		group.addMember(users[1]);
		groupRepository.save(group);

		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete(GROUP_ENDPOINT_URL + group.getId() + "/user/" + users[1].getId())
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();

		//assert
		Assert.assertEquals("wrong response status", 200, status);
		for (int index = 0; index < 2; index++) {
			Assert.assertTrue("user"+ index + " shouldn't get deleted when removing a member from a group", userRepository.findById(users[index].getId()) != null);
		}
		Assert.assertTrue("group shouldn't get deleted when deleting its member", groupRepository.findById(group.getId()) != null);
		for (User user: group.getMembers()) {
			Assert.assertNotEquals("group shouldn't reference the deleted member", users[1].getId(), user.getId());
		}
		for (Group userGroup: users[1].getMemberGroups()) {
			Assert.assertNotEquals("user shouldn't reference the group it was deleted from", group.getId(), userGroup.getId());
		}	
	}
	
	@Test
	public void testListMembers() throws Exception {
    	//prepare
		Principal mockPrincipal = Mockito.mock(Principal.class);
        User[] users = new User[3];
        String[] usernames = createUniqueEmail(3);
        String[] user_names = createUniqueAlphanumericString(10, 3);
        for (int index = 0; index < 3; index++) {
        	users[index] = createUser(usernames[index], user_names[index], null, null);
        }
		Mockito.when(mockPrincipal.getName()).thenReturn(usernames[0]);
		Group group = createGroup(createUniqueAlphanumericString(10), createUniqueAlphanumericString(50), null, users[0]);
		group.addMember(users[1]);
		group.addMember(users[2]);
		groupRepository.save(group);

		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(GROUP_ENDPOINT_URL + group.getId() + "/user/")
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		JSONArray content = new JSONArray(response.getContentAsString());
		
		//assert
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong number of members", 3, content.length()); // one user is the admin
		for (int index = 0; index < 3; index++) {
			JSONObject resMember = (JSONObject)content.get(index);
			Assert.assertEquals("wrong name is returned for the member" + index, user_names[index], (String)resMember.get("name"));			
		}
	}
}