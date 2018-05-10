package com.boot.cut_costs.controller;

import java.security.Principal;
import java.util.Arrays;
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

import com.boot.cut_costs.model.Expense;
import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.User;

public class ExpenseControllerTest extends BaseControllerTest {

	@Test
	public void testList() throws Exception {
		//prepare
		Principal mockPrincipal = Mockito.mock(Principal.class);
        User[] users = new User[3];
        String[] usernames = createUniqueEmail(3);
        for (int index = 0; index < 3; index++) {
        	users[index] = createUser(usernames[index], createUniqueAlphanumericString(10), null, null);
        }
		Mockito.when(mockPrincipal.getName()).thenReturn(usernames[0]);
		Group[] groups = new Group[2];
		for (int index = 0; index < 2; index++) {
			groups[index] = createGroup(createUniqueAlphanumericString(10), null, null, users[0]);
		}
		String[] expense_titles = createUniqueAlphanumericString(10, 3);
		User[] expense_owners = new User[] {users[0], users[0], users[1]};
		Group[] expense_groups = new Group[] {groups[0], groups[0], groups[1]};
		Long[][] sharer_ids = new Long[][] {
				new Long[] { users[0].getId(), users[1].getId() },
				new Long[] { users[2].getId() },
				new Long[] { users[0].getId(), users[2].getId() } };
		String[] expense_descriptions = createUniqueAlphanumericString(30, 3);
		Expense[] expenses = new Expense[3];
		for (int index = 0; index < 3; index++) {
			//TODO fix here
			expenses[index] = createExpense(
					expense_titles[index], index, expense_descriptions[index], null,
					Arrays.asList(sharer_ids[index]), expense_owners[index], expense_groups[index].getId());
		}

		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(EXPENSE_ENDPOINT_URL)
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		JSONArray content = new JSONArray(response.getContentAsString());

		//assert
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong number of expenses returned", 3, content.length());
		int expectedIndex = 0;
		for (int actualIndex : new int[] {2,0,1}) { // first comes the received expenses
			JSONObject responseExpense = ((JSONObject)content.get(expectedIndex));
			Assert.assertEquals("wrong title for expense " + actualIndex, expense_titles[actualIndex], responseExpense.get("title"));
			Assert.assertEquals("wrong amount for expense " + actualIndex, actualIndex, responseExpense.get("amount"));
			expectedIndex++;
		}
	}

	@Test
	//TODO fix here
	public void testUpdate() throws Exception {
		//WRITE TEST HERE
	}
	
	@Test
	public void testCreate() throws Exception {
		//prepare
    	Principal mockPrincipal = Mockito.mock(Principal.class);
        User[] users = new User[2];
        String[] usernames = createUniqueEmail(2);
        for (int index = 0; index < 2; index++) {
        	users[index] = createUser(usernames[index], createUniqueAlphanumericString(10), null, null);
        }
        Mockito.when(mockPrincipal.getName()).thenReturn(usernames[0]);
        Group group = createGroup(createUniqueAlphanumericString(15), null, null, users[0]);
        group.addMember(users[1]);
        String expense_title = createUniqueAlphanumericString(10);
        String expense_description = createUniqueAlphanumericString(35);
        long expense_amount = 120;
        JSONObject jo = new JSONObject();
		jo.put(TITLE_FIELD_NAME, expense_title);
		jo.put(DESCRIPTION_FIELD_NAME, expense_description);
		jo.put(IMAGE_FIELD_NAME, DUMMY_IMAGE);
		jo.put(AMOUNT_FIELD_NAME, expense_amount);
		jo.put(SHARERS_FIELD_NAME, Arrays.asList(users[0].getId(), users[1].getId()));
		String testExpenseJson = jo.toString();

		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(EXPENSE_ENDPOINT_URL + group.getId() + "/expense").content(testExpenseJson)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		//assert
		Assert.assertEquals("wrong response status", 200, response.getStatus());
		List<Expense> expenses = Lists.newArrayList(expenseRepository.findAll());
		Assert.assertEquals("wrong number of expenses created", 1, expenses.size());

		List<Expense> groupExpenses = groupRepository.findOne(group.getId()).getExpenses();
		Assert.assertEquals("wrong number of expenses created for its associated group", 1, groupExpenses.size());
		Expense groupExpense = groupExpenses.get(0);
		Assert.assertEquals("wrong title set for the group expense", expense_title, groupExpense.getTitle());
		Assert.assertEquals("wrong description set for the group expense", expense_description, groupExpense.getDescription());
		Assert.assertEquals("wrong amount set for the group expense", expense_amount, groupExpense.getAmount());
//		Assert.assertTrue("image file not created", CommonUtils.getImageFile(groupExpense.getImageId()).exists());

		List<Expense> userExpenses = userRepository.findOne(users[0].getId()).getOwnedExpenses();
		Assert.assertEquals("wrong number of expenses created for user's associated group", 1, userExpenses.size());
		Expense userExpense = userExpenses.get(0);
		Assert.assertEquals("wrong expense title assigned to the user", expense_title, userExpense.getTitle());
		Assert.assertEquals("wrong expense description assigned to the user", expense_description, userExpense.getDescription());
		Assert.assertEquals("wrong expense amount assigned to the user", expense_amount, userExpense.getAmount());

		Assert.assertEquals("wrong expense assigned to the user", expense_title, userRepository.findOne(users[1].getId()).getReceivedExpenses().get(0).getTitle());
	}
	
	@Test
	public void testDelete() throws Exception {
		//prepare
    	Principal mockPrincipal = Mockito.mock(Principal.class);
    	String[] usernames = createUniqueEmail(3);
        Mockito.when(mockPrincipal.getName()).thenReturn(usernames[0]);
        User[] users = new User[3];
        for (int index = 0; index < 3; index++) {
        	users[index] = createUser(usernames[index], createUniqueAlphanumericString(10), null, null);
        }
        Group[] groups = new Group[2];
        for (int index = 0; index < 2; index++) {
    		groups[index] = createGroup(createUniqueAlphanumericString(10), null, null, users[index]);        	
        }
		User[] expense_owners = new User[] {users[0], users[0], users[0]};
		Group[] expense_groups = new Group[] {groups[0], groups[0], groups[1]};
		Long[][] sharer_ids = new Long[][] {
				new Long[] { users[1].getId(), users[2].getId() },
				new Long[] { users[0].getId(), users[1].getId(), users[2].getId() },
				new Long[] { users[1].getId(), users[2].getId() } };
        Expense[] expenses = new Expense[3];
        for (int index = 0; index < 3; index++) {
			expenses[index] = createExpense(createUniqueAlphanumericString(10),
					index, null, null, Arrays.asList(sharer_ids[index]),
					expense_owners[index], expense_groups[index].getId());
        }
		groups[0].addMember(users[1]);
		groups[0].addMember(users[2]);
		groups[1].addMember(users[0]);
		groups[1].addMember(users[1]);

		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete(EXPENSE_ENDPOINT_URL + expenses[0].getId())
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		//assert
		Assert.assertEquals("wrong response status", 200, response.getStatus());
		Assert.assertEquals("wrong response content", "", response.getContentAsString());
		for (int index = 0; index < 3; index++) {
			 List<Expense> userExpenses = userRepository.findById(users[index].getId()).getAllExpenses();
			Assert.assertEquals("wrong number of expenses for user" + index, 2, userExpenses.size());
			for (Expense exp: userExpenses) {
				Assert.assertTrue("deleted expense is still available for user" + index, exp.getId() != expenses[0].getId());
			}			
		}
		Assert.assertEquals("wrong number of expenses", 1, groupRepository.findById(groups[0].getId()).getExpenses().size());
	}

	@Test
	public void testDeleteGroupWithExpense() throws Exception {
		//prepare
    	Principal mockPrincipal = Mockito.mock(Principal.class);
    	String[] usernames = createUniqueEmail(3);
        Mockito.when(mockPrincipal.getName()).thenReturn(usernames[0]);
        User[] users = new User[3];
        for (int index = 0; index < 3; index++) {
        	users[index] = createUser(usernames[index], createUniqueAlphanumericString(10), null, null);
        }
        Group[] groups = new Group[2];
        User[] admins = new User[]{users[0], users[1]};
        for (int index = 0; index < 2; index++) {
        	groups[index] = createGroup(createUniqueAlphanumericString(10), null, null, admins[index]);
        }
		Group[] expense_groups = new Group[] {groups[0], groups[1]};
		Long[][] sharer_ids = new Long[][] {
				new Long[] { users[1].getId(), users[2].getId() },
				new Long[] { users[0].getId(), users[2].getId() }};
        User[] expense_owners = new User[] {users[0], users[2]};
		Expense[] expenses = new Expense[2];
        for (int index = 0; index < 2; index++) {
			expenses[index] = createExpense(createUniqueAlphanumericString(10),
					index, null, null, Arrays.asList(sharer_ids[index]),
					expense_owners[index], expense_groups[index].getId());
        }
		groups[0].addMember(users[1]);
		groups[0].addMember(users[2]);
		groups[1].addMember(users[0]);
		groups[1].addMember(users[1]);

		//action
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete(GROUP_ENDPOINT_URL + groups[0].getId())
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		//assert
		Assert.assertEquals("wrong response status", 200, response.getStatus());
		Assert.assertEquals("wrong response content", "", response.getContentAsString());
		Assert.assertEquals("wrong number of expenses for user0", 1, userRepository.findById(users[0].getId()).getReceivedExpenses().size());
		Assert.assertEquals("wrong number of expenses for user1", 0, userRepository.findById(users[1].getId()).getReceivedExpenses().size());
		Assert.assertEquals("wrong number of expenses for user2", 1, userRepository.findById(users[2].getId()).getOwnedExpenses().size());
		Assert.assertEquals("wrong number of expenses exist", 1, Lists.newArrayList(expenseRepository.findAll()).size());
	}
}
