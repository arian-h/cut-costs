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
import com.boot.cut_costs.model.Invitation;
import com.boot.cut_costs.model.User;

public class InvitationControllerTest extends BaseControllerTest {
	
	@Test
	public void testCreate() throws Exception {
		//prepare
		Principal mockPrincipal = Mockito.mock(Principal.class);
        User admin = createUser(createUniqueEmail(), createUniqueAlphanumericString(10), null, null);
        String username = createUniqueEmail();
        User inviter = createUser(username, createUniqueAlphanumericString(10), null, null);
		Mockito.when(mockPrincipal.getName()).thenReturn(username);
        User invitee = createUser(createUniqueEmail(), createUniqueAlphanumericString(10), null, null);
        Group group = createGroup(createUniqueAlphanumericString(10), null, null, admin);
        group.addMember(inviter);
        JSONObject jo = new JSONObject();
        String invitation_description = createUniqueAlphanumericString(50);
		jo.put(INVITEE_ID_FIELD_NAME, invitee.getId());
		jo.put(GROUP_ID_FIELD_NAME, group.getId());
		jo.put(DESCRIPTION_FIELD_NAME, invitation_description);

		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(INVITATION_ENDPOINT).content(jo.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();

		//assert
		Assert.assertEquals("wrong response status", 200, status);
		List<Invitation> invitations = Lists.newArrayList(invitationRepository.findAll());
		Assert.assertEquals("wrong number of invitations created", 1, invitations.size());
		Invitation invitation = invitations.get(0);
		Assert.assertEquals("wrong invitee assigned to the invitation", invitee.getId(), invitation.getInvitee().getId());
		Assert.assertEquals("wrong inviter assigned to the invitation", inviter.getId(), invitation.getInviter().getId());
		Assert.assertEquals("wrong group assigned to the invitation", group.getId(), invitation.getGroup().getId());
		User actualInvitee = userRepository.findById(invitee.getId());
		Assert.assertEquals("wrong number of received invitations for the invitee", 1, actualInvitee.getReceivedInvitations().size());		
		User actualInviter = userRepository.findById(inviter.getId());
		Assert.assertEquals("wrong number of owned invitations for the inviter", 1, actualInviter.getOwnedInvitations().size());		
	}
	
	@Test
	public void testList() throws Exception {
		//prepare
		Principal mockPrincipal = Mockito.mock(Principal.class);
        User admin = createUser(createUniqueEmail(), createUniqueAlphanumericString(10), null, null);
        String[] usernames = createUniqueEmail(2);
        User[] users = new User[2];
        for (int index = 0; index < 2; index++) {
        	users[index] = createUser(usernames[index], createUniqueAlphanumericString(10), null, null);
        }
		Mockito.when(mockPrincipal.getName()).thenReturn(usernames[0]);
        Group[] groups = new Group[2];
        for (int index = 0; index < 2; index++) {
        	groups[index] = createGroup(createUniqueAlphanumericString(10), null, null, admin);
        }
        long[] inviterIds = new long[] {users[0].getId(), users[1].getId()};
        long[] inviteeIds = new long[] {users[1].getId(), users[0].getId()};
        for (int index = 0; index < 2; index++) {
            createInvitation(inviterIds[index], inviteeIds[index], groups[index].getId());        	
        }

        //action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(INVITATION_ENDPOINT)
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		JSONArray content = new JSONArray(response.getContentAsString());

		//assert
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong number of invitations", 1, content.length());
		JSONObject invitationResponse = content.getJSONObject(0);
		Assert.assertEquals("wrong inviter is assigned to the invitation", inviterIds[1], invitationResponse.getJSONObject("inviter").getLong("id"));
		Assert.assertEquals("wrong group is assigned to the invitation", groups[1].getId(), invitationResponse.getJSONObject("group").getLong("id"));
	}
	
	@Test
	public void testAccept() throws Exception {
		//prepare
		Principal mockPrincipal = Mockito.mock(Principal.class);
        User admin = createUser(createUniqueEmail(), createUniqueAlphanumericString(10), null, null);
        String[] usernames = createUniqueEmail(3);
        User[] users = new User[2];
        for (int index = 0; index < 2; index++) {
        	users[index] = createUser(usernames[index], createUniqueAlphanumericString(10), null, null);
        }
		Mockito.when(mockPrincipal.getName()).thenReturn(usernames[0]);
        Group group = createGroup(createUniqueAlphanumericString(10), null, null, admin);
        group.addMember(users[1]);
        Invitation invitation = createInvitation(users[1].getId(), users[0].getId(), group.getId());

        //action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(INVITATION_ENDPOINT + invitation.getId() + "/accept")
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		String content = response.getContentAsString();

		//assert
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong number of invitations", "", content);
		Group targetGroup = groupRepository.findById(group.getId());
		Assert.assertEquals("wrong number of group members", 2, targetGroup.getMembers().size());
		Assert.assertTrue("invitee was not added to the group", targetGroup.getMembers().contains(users[1]));
		Assert.assertFalse("invitee has invitation in its list of invitations", users[1].getReceivedInvitations().contains(invitation));
		Assert.assertTrue("invitee doesn't have target group in its list of groups", userRepository.findById(users[0].getId()).getMemberGroups().contains(group));
		Assert.assertFalse("invitation exists in the db", invitationRepository.exists(invitation.getId()));	
	}
	
	@Test
	public void testReject() throws Exception {
		//prepare
		Principal mockPrincipal = Mockito.mock(Principal.class);
        User admin = createUser(createUniqueEmail(), createUniqueAlphanumericString(10), null, null);
        String[] usernames = createUniqueEmail(3);
        User[] users = new User[2];
        //user[1] invites user[0] to the group
        for (int index = 0; index < 2; index++) {
        	users[index] = createUser(usernames[index], createUniqueAlphanumericString(10), null, null);
        }
		Mockito.when(mockPrincipal.getName()).thenReturn(usernames[0]);
        Group group = createGroup(createUniqueAlphanumericString(10), null, null, admin);
        group.addMember(users[1]);
        Invitation invitation = createInvitation(users[1].getId(), users[0].getId(), group.getId());

        //action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(INVITATION_ENDPOINT + invitation.getId() + "/reject")
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		String content = response.getContentAsString();

		//assert
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong number of invitations", "", content);
		Group targetGroup = groupRepository.findById(group.getId());
		Assert.assertEquals("wrong number of group members", 1, targetGroup.getMembers().size());
		Assert.assertFalse("invitee added to the group", targetGroup.getMembers().contains(users[0]));
		Assert.assertFalse("invitee has invitation in its list of invitations", users[0].getReceivedInvitations().contains(invitation));
		Assert.assertFalse("invitee doesn't have target group in its list of groups", userRepository.findById(users[0].getId()).getMemberGroups().contains(group));
		Assert.assertFalse("invitation exists in the db", invitationRepository.exists(invitation.getId()));	
	}
}
