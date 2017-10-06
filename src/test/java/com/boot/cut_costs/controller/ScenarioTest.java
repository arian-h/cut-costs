package com.boot.cut_costs.controller;

import java.security.Principal;
import java.util.Arrays;

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

public class ScenarioTest extends BaseControllerTest {
	
	@Test
	public void test1() throws Exception {
    	String[] usernames = createUniqueEmail(4);
        User[] users = new User[4];
        for (int index = 0; index < 4; index++) {
        	users[index] = createUser(usernames[index], createUniqueAlphanumericString(10), null, null);
        }
		Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(usernames[0]);
        //user0 creates group0
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
		JSONObject content = new JSONObject(response.getContentAsString());
		long groupId = content.getLong("id");
		Assert.assertEquals("wrong response status", 200, status);
		
		long[] invitationId = new long[3];
		
		//user0 invites user1 to group0
		jo.put(INVITEE_ID_FIELD_NAME, users[1].getId());
		jo.put(GROUP_ID_FIELD_NAME, groupId);
		jo.put(DESCRIPTION_FIELD_NAME, null);
		//action
		requestBuilder = MockMvcRequestBuilders
				.post(INVITATION_ENDPOINT).content(jo.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		result = mockMvc.perform(requestBuilder).andReturn();
		response = result.getResponse();
		status = response.getStatus();
		content = new JSONObject(response.getContentAsString());
		invitationId[0] = content.getLong("id");

		Assert.assertEquals("wrong response status", 200, status);
		
		//user0 invites user2 to group0
		jo.put(INVITEE_ID_FIELD_NAME, users[2].getId());
		jo.put(GROUP_ID_FIELD_NAME, groupId);
		jo.put(DESCRIPTION_FIELD_NAME, null);
		//action
		requestBuilder = MockMvcRequestBuilders
				.post(INVITATION_ENDPOINT).content(jo.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		result = mockMvc.perform(requestBuilder).andReturn();
		response = result.getResponse();
		status = response.getStatus();
		content = new JSONObject(response.getContentAsString());
		invitationId[1] = content.getLong("id");

		Assert.assertEquals("wrong response status", 200, status);

		//user0 invites user3 to group0
		jo.put(INVITEE_ID_FIELD_NAME, users[3].getId());
		jo.put(GROUP_ID_FIELD_NAME, groupId);
		jo.put(DESCRIPTION_FIELD_NAME, null);
		//action
		requestBuilder = MockMvcRequestBuilders
				.post(INVITATION_ENDPOINT).content(jo.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		result = mockMvc.perform(requestBuilder).andReturn();
		response = result.getResponse();
		status = response.getStatus();
		content = new JSONObject(response.getContentAsString());
		invitationId[2] = content.getLong("id");

		Assert.assertEquals("wrong response status", 200, status);

		//user3 rejects invitation2
        Mockito.when(mockPrincipal.getName()).thenReturn(usernames[3]);
		requestBuilder = MockMvcRequestBuilders
				.get(INVITATION_ENDPOINT + invitationId[2] + "/reject")
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals("wrong response status", 200, result.getResponse().getStatus());

		//user1 accepts invitation0
        Mockito.when(mockPrincipal.getName()).thenReturn(usernames[1]);
		requestBuilder = MockMvcRequestBuilders
				.get(INVITATION_ENDPOINT + invitationId[0] + "/accept")
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals("wrong response status", 200, result.getResponse().getStatus());
		
        Mockito.when(mockPrincipal.getName()).thenReturn(usernames[0]);

		//user0 posts an expense to group, sharers are user0, user1, user3
        jo = new JSONObject();
		jo.put(TITLE_FIELD_NAME, createUniqueAlphanumericString(10));
		jo.put(DESCRIPTION_FIELD_NAME, createUniqueAlphanumericString(35));
		jo.put(IMAGE_FIELD_NAME, DUMMY_IMAGE);
		jo.put(AMOUNT_FIELD_NAME, 120);
		jo.put(SHARERS_FIELD_NAME, Arrays.asList(users[0].getId(), users[1].getId(), users[3].getId()));

		requestBuilder = MockMvcRequestBuilders
				.post(EXPENSE_ENDPOINT_URL + groupId + "/expense").content(jo.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals("wrong response status", 403, result.getResponse().getStatus());
		
//		//user0 posts an expense to group, sharers are user0, user1, user2
        jo = new JSONObject();
		jo.put(TITLE_FIELD_NAME, createUniqueAlphanumericString(10));
		jo.put(DESCRIPTION_FIELD_NAME, createUniqueAlphanumericString(35));
		jo.put(IMAGE_FIELD_NAME, DUMMY_IMAGE);
		jo.put(AMOUNT_FIELD_NAME, 120);
		jo.put(SHARERS_FIELD_NAME, Arrays.asList(users[0].getId(), users[1].getId(), users[2].getId()));

		requestBuilder = MockMvcRequestBuilders
				.post(EXPENSE_ENDPOINT_URL + groupId + "/expense").content(jo.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals("wrong response status", 403, result.getResponse().getStatus());
		
		//user2 accepts invitation1
        Mockito.when(mockPrincipal.getName()).thenReturn(usernames[2]);
		requestBuilder = MockMvcRequestBuilders
				.get(INVITATION_ENDPOINT + invitationId[1] + "/accept")
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals("wrong response status", 200, result.getResponse().getStatus());
	}
}
