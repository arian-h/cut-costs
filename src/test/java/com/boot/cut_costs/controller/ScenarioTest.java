package com.boot.cut_costs.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
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
		ArrayList<String> expenseTitles = new ArrayList<String>();
		ArrayList<Integer> expenseAmounts = new ArrayList<Integer>();
		Map<Integer, Integer> expenseIds = new HashMap<Integer, Integer>();

        //user0 creates group
        String groupName = createUniqueAlphanumericString(10);
        MockHttpServletResponse response = createGroup(mockPrincipal, usernames[0], groupName);

		Assert.assertEquals("wrong response status", 200, response.getStatus());
		long groupId = new JSONObject(response.getContentAsString()).getLong("id");
		
		long[] invitationId = new long[3];
		
		//user0 invites user1 to group
		response = invite(mockPrincipal, usernames[0], users[1].getId(), groupId);
		Assert.assertEquals("wrong response status", 200, response.getStatus());
		invitationId[0] = new JSONObject(response.getContentAsString()).getLong("id");
		
		//user0 invites user2 to group
		response = invite(mockPrincipal, usernames[0], users[2].getId(), groupId);
		Assert.assertEquals("wrong response status", 200, response.getStatus());
		invitationId[1] = new JSONObject(response.getContentAsString()).getLong("id");

		//user0 invites user3 to group
		response = invite(mockPrincipal, usernames[0], users[3].getId(), groupId);
		Assert.assertEquals("wrong response status", 200, response.getStatus());
		invitationId[2] = new JSONObject(response.getContentAsString()).getLong("id");
		
		//user3 rejects invitation2
        response = rejectInvitation(mockPrincipal, usernames[3], invitationId[2]);
		Assert.assertEquals("wrong response status", 200, response.getStatus());
		
		//user1 accepts invitation0
		response = acceptInvitation(mockPrincipal, usernames[1], invitationId[0]);
		Assert.assertEquals("wrong response status", 200, response.getStatus());

		//user0 posts an expense to group, sharers are user0, user1, user3
        response = postExpense(mockPrincipal, usernames[0], groupId, new long[] {users[0].getId(), users[1].getId(), users[3].getId()}, createUniqueAlphanumericString(10), 120);
		Assert.assertEquals("wrong response status", 403, response.getStatus());
		
		//user0 posts an expense to group, sharers are user0, user1, user2
        response = postExpense(mockPrincipal, usernames[0], groupId, new long[] {users[0].getId(), users[1].getId(),users[2].getId()}, createUniqueAlphanumericString(10), 120);
		Assert.assertEquals("wrong response status", 403, response.getStatus());
		
		//user2 accepts invitation1
		response = acceptInvitation(mockPrincipal, usernames[2], invitationId[1]);
		Assert.assertEquals("wrong response status", 200, response.getStatus());
		
		//user0 posts an expense to group, sharers are user0, user1, user2
		String expenseTitle = createUniqueAlphanumericString(10);
		expenseTitles.add(expenseTitle);
        int expenseAmount = (int) (Math.random() * 100 + 1);
        expenseAmounts.add(expenseAmount);
		response = postExpense(mockPrincipal, usernames[0], groupId, new long[] {users[0].getId(), users[1].getId(),users[2].getId()}, expenseTitle, expenseAmount);
		Assert.assertEquals("wrong response status", 200, response.getStatus());
		expenseIds.put(0, (int)new JSONObject(response.getContentAsString()).get("id"));
		
		//user1 posts an expense to group, sharer is user0
		expenseTitle = createUniqueAlphanumericString(10);
        expenseTitles.add(expenseTitle);
        expenseAmount = (int) (Math.random() * 100 + 1);
        expenseAmounts.add(expenseAmount);
		response = postExpense(mockPrincipal, usernames[1], groupId, new long[] {users[0].getId()}, expenseTitle, expenseAmount);
		Assert.assertEquals("wrong response status", 200, response.getStatus());
		expenseIds.put(1, (int)new JSONObject(response.getContentAsString()).get("id"));

		//user2 posts an expense to group, sharers are user0, user1
		expenseTitle = createUniqueAlphanumericString(10);
        expenseTitles.add(expenseTitle);
        expenseAmount = (int) (Math.random() * 100 + 1);
        expenseAmounts.add(expenseAmount);
		response = postExpense(mockPrincipal, usernames[2], groupId, new long[] {users[0].getId(), users[1].getId()}, expenseTitle, expenseAmount);
		Assert.assertEquals("wrong response status", 200, response.getStatus());
		expenseIds.put(2, (int)new JSONObject(response.getContentAsString()).get("id"));

        //user2 gets list of expenses
		response = listExpenses(mockPrincipal, usernames[2]);
		JSONArray contentArray = new JSONArray(response.getContentAsString());
		Assert.assertEquals("wrong response status", 200, response.getStatus());
		Assert.assertEquals("wrong number of expenses returned", 2, contentArray.length());
		int actualIndex = 0;
		for (int expectedIndex : new int[] {0, 2}) { // first comes the received expenses
			JSONObject responseExpense = ((JSONObject)contentArray.get(actualIndex));
			Assert.assertEquals("wrong title for expense ", expenseTitles.get(expectedIndex), responseExpense.get("title"));
			Assert.assertEquals("wrong amount for expense ", expenseAmounts.get(expectedIndex), responseExpense.get("amount"));
			actualIndex++;
		}
		
        //user0 gets list of expenses
		response = listExpenses(mockPrincipal, usernames[0]);
		contentArray = new JSONArray(response.getContentAsString());
		Assert.assertEquals("wrong response status", 200, response.getStatus());
		Assert.assertEquals("wrong number of expenses returned", 3, contentArray.length());
		actualIndex = 0;
		for (int expectedIndex : new int[] {1, 2, 0}) { // first comes the received expenses
			JSONObject responseExpense = ((JSONObject)contentArray.get(actualIndex));
			Assert.assertEquals("wrong title for expense ", expenseTitles.get(expectedIndex), responseExpense.get("title"));
			Assert.assertEquals("wrong amount for expense ", expenseAmounts.get(expectedIndex), responseExpense.get("amount"));
			actualIndex++;
		}
		
        //user1 gets list of expenses
		response = listExpenses(mockPrincipal, usernames[1]);
		contentArray = new JSONArray(response.getContentAsString());
		Assert.assertEquals("wrong response status", 200, response.getStatus());
		Assert.assertEquals("wrong number of expenses returned", 3, contentArray.length());
		actualIndex = 0;
		for (int expectedIndex : new int[] {0, 2, 1}) { // first comes the received expenses
			JSONObject responseExpense = ((JSONObject)contentArray.get(actualIndex));
			Assert.assertEquals("wrong title for expense ", expenseTitles.get(expectedIndex), responseExpense.get("title"));
			Assert.assertEquals("wrong amount for expense ", expenseAmounts.get(expectedIndex), responseExpense.get("amount"));
			actualIndex++;
		}

		//user1 deletes expense0
		response = deleteExpense(mockPrincipal, usernames[1], expenseIds.get(0));
		Assert.assertEquals("wrong response status", 400, response.getStatus());

		//user1 deletes expense0
		response = deleteExpense(mockPrincipal, usernames[0], expenseIds.get(1));
		Assert.assertEquals("wrong response status", 200, response.getStatus());

        //user0 gets list of expenses
		response = listExpenses(mockPrincipal, usernames[0]);
		contentArray = new JSONArray(response.getContentAsString());
		Assert.assertEquals("wrong response status", 200, response.getStatus());
		Assert.assertEquals("wrong number of expenses returned", 2, contentArray.length());
		actualIndex = 0;
		for (int expectedIndex : new int[] {2, 0}) { // first comes the received expenses
			JSONObject responseExpense = ((JSONObject)contentArray.get(actualIndex));
			Assert.assertEquals("wrong title for expense ", expenseTitles.get(expectedIndex), responseExpense.get("title"));
			Assert.assertEquals("wrong amount for expense ", expenseAmounts.get(expectedIndex), responseExpense.get("amount"));
			actualIndex++;
		}
		
		//user1 attempts to delete group
		response = deleteGroup(mockPrincipal, usernames[1], groupId);
		Assert.assertEquals("wrong response status", 403, response.getStatus());

		//user0 deletes group
		response = deleteGroup(mockPrincipal, usernames[0], groupId);
		Assert.assertEquals("wrong response status", 200, response.getStatus());
		
        //user0 gets list of expenses
		response = listExpenses(mockPrincipal, usernames[0]);
		contentArray = new JSONArray(response.getContentAsString());
		Assert.assertEquals("wrong number of expenses returned", 0, contentArray.length());
		
        //user1 gets list of expenses
		response = listExpenses(mockPrincipal, usernames[1]);
		contentArray = new JSONArray(response.getContentAsString());
		Assert.assertEquals("wrong number of expenses returned", 0, contentArray.length());
		
		//user2 gets list of expenses
		response = listExpenses(mockPrincipal, usernames[2]);
		contentArray = new JSONArray(response.getContentAsString());
		Assert.assertEquals("wrong number of expenses returned", 0, contentArray.length());
	}

	private MockHttpServletResponse createGroup(Principal mockPrincipal, String username, String groupName) throws Exception {
		Mockito.when(mockPrincipal.getName()).thenReturn(username);
		JSONObject jo = new JSONObject();
		jo.put(NAME_FIELD_NAME, groupName);
		jo.put(IMAGE_FIELD_NAME, DUMMY_IMAGE);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(GROUP_ENDPOINT_URL).content(jo.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}
	
	private MockHttpServletResponse invite(Principal mockPrincipal, String inviterName, long inviteeId, long groupId) throws Exception {
		Mockito.when(mockPrincipal.getName()).thenReturn(inviterName);
		JSONObject jo = new JSONObject();
		jo.put(INVITEE_ID_FIELD_NAME, inviteeId);
		jo.put(GROUP_ID_FIELD_NAME, groupId);
		jo.put(DESCRIPTION_FIELD_NAME, null);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(INVITATION_ENDPOINT).content(jo.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}
	
	private MockHttpServletResponse rejectInvitation(Principal mockPrincipal, String username, long invitationId) throws Exception {
        Mockito.when(mockPrincipal.getName()).thenReturn(username);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(INVITATION_ENDPOINT + invitationId + "/reject")
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}
	
	private MockHttpServletResponse acceptInvitation(Principal mockPrincipal, String username, long invitationId) throws Exception {
        Mockito.when(mockPrincipal.getName()).thenReturn(username);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(INVITATION_ENDPOINT + invitationId + "/accept")
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}

	private MockHttpServletResponse postExpense(Principal mockPrincipal, String username, long groupId, long[] sharers, String title, long amount) throws Exception {
        Mockito.when(mockPrincipal.getName()).thenReturn(username);
		JSONObject jo = new JSONObject();
		jo.put(TITLE_FIELD_NAME, title);
		jo.put(AMOUNT_FIELD_NAME, amount);
		List<Long> sharers_ids = new ArrayList<Long>();
		for (long sharer_id: sharers) {
			sharers_ids.add(sharer_id);
		}
		jo.put(SHARERS_FIELD_NAME, sharers_ids);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(EXPENSE_ENDPOINT_URL + groupId + "/expense").content(jo.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}
	
	private MockHttpServletResponse listExpenses(Principal mockPrincipal, String username) throws Exception {
		Mockito.when(mockPrincipal.getName()).thenReturn(username);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(EXPENSE_ENDPOINT_URL)
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}
	
	private MockHttpServletResponse deleteExpense(Principal mockPrincipal, String username, long expenseId) throws Exception {
		Mockito.when(mockPrincipal.getName()).thenReturn(username);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete(EXPENSE_ENDPOINT_URL + expenseId)
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}
	
	private MockHttpServletResponse deleteGroup(Principal mockPrincipal, String username, long groupId) throws Exception {
		Mockito.when(mockPrincipal.getName()).thenReturn(username);
		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete(GROUP_ENDPOINT_URL + groupId)
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}
}
