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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.boot.cut_costs.model.Expense;
import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.ExpenseRepository;
import com.boot.cut_costs.repository.GroupRepository;
import com.boot.cut_costs.repository.UserRepository;
import com.boot.cut_costs.utils.ExpenseTestingUtil;
import com.boot.cut_costs.utils.GroupTestingUtil;
import com.boot.cut_costs.utils.UserTestingUtil;

public class ExpenseControllerTest extends BaseControllerTest {

	@Autowired
	private UserTestingUtil userTestingUtil;
	
	@Autowired
	private GroupTestingUtil groupTestingUtil;

	@Autowired
	private ExpenseTestingUtil expenseTestingUtil;

	@Autowired 
	private GroupRepository groupRepository;
	
	@Autowired 
	private UserRepository userRepository;

	@Autowired
	private ExpenseRepository expenseRepository;

	private final static String DESCRIPTION_FIELD_NAME = "description";
	private final static String IMAGE_FIELD_NAME = "image";
	private final static String TITLE_FIELD_NAME = "title";

	private final static String DUMMY_USERNAME = "test@test.com";
	private final static String DUMMY_USER_NAME = "testUser";
	private final static String DUMMY_GROUP_NAME = "dummygroupname";
	private final static String DUMMY_EXPENSE_TITLE = "dummyexpensetitle";
	private final static String DUMMY_EXPENSE_DESCRIPTION = "dummyexpensedescription";
	private final static String DUMMY_IMAGE_ID = "123456789012345";
	private final static long DUMMY_EXPENSE_AMOUNT = 12;
	private final static String DUMMY_IMAGE = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAADklEQVQIW2NgQAXGZHAAGioAza6+Hk0AAAAASUVORK5CYII=";
	private final static String AMOUNT_FIELD_NAME = "amount";
	private final static String SHARERS_FIELD_NAME = "sharers";
	
	private final static String EXPENSE_ENDPOINT_URL = "/expense/";

	@Test
	public void testGetAllExpenses() throws Exception {
    	Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(DUMMY_USERNAME + "1");

		User user1 = userTestingUtil.createUser(DUMMY_USERNAME + "1", DUMMY_USER_NAME + "1", null, null);
		User user2 = userTestingUtil.createUser(DUMMY_USERNAME + "2", DUMMY_USER_NAME + "2", null, null);
		User user3 = userTestingUtil.createUser(DUMMY_USERNAME + "3", DUMMY_USER_NAME + "3", null, null);
		Group group1 = groupTestingUtil.createGroup(DUMMY_GROUP_NAME + "1", null, null, user1);
		Group group2 = groupTestingUtil.createGroup(DUMMY_GROUP_NAME + "2", null, null, user1);

		Expense expense1 = expenseTestingUtil.createExpense(
				DUMMY_EXPENSE_TITLE + "1", DUMMY_EXPENSE_AMOUNT + 1, DUMMY_EXPENSE_DESCRIPTION + "1", DUMMY_IMAGE_ID,
				Arrays.asList(user1.getId(), user2.getId()), user1, group1.getId());
		Expense expense2 = expenseTestingUtil.createExpense(
				DUMMY_EXPENSE_TITLE + "2", DUMMY_EXPENSE_AMOUNT + 2, DUMMY_EXPENSE_DESCRIPTION + "2", DUMMY_IMAGE_ID,
				Arrays.asList(user3.getId()), user1, group1.getId());
		Expense expense3 = expenseTestingUtil.createExpense(
				DUMMY_EXPENSE_TITLE + "3", DUMMY_EXPENSE_AMOUNT + 3, DUMMY_EXPENSE_DESCRIPTION + "3", DUMMY_IMAGE_ID,
				Arrays.asList(user1.getId(), user3.getId()), user2, group2.getId());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(EXPENSE_ENDPOINT_URL)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		JSONArray content = new JSONArray(response.getContentAsString());
		
		Assert.assertEquals("wrong response status", 200, status);
		Assert.assertEquals("wrong number of expenses returned", 3, content.length());
		Assert.assertEquals("wrong expense returned", expense1.getTitle(), ((JSONObject)content.get(0)).get("title"));
		Assert.assertEquals("wrong expense returned", (int)expense1.getAmount(), ((JSONObject)content.get(0)).get("amount"));

		Assert.assertEquals("wrong expense returned", expense2.getTitle(), ((JSONObject)content.get(1)).get("title"));
		Assert.assertEquals("wrong expense returned", (int)expense2.getAmount(), ((JSONObject)content.get(1)).get("amount"));

		Assert.assertEquals("wrong expense returned", expense3.getTitle(), ((JSONObject)content.get(2)).get("title"));
		Assert.assertEquals("wrong expense returned", (int)expense3.getAmount(), ((JSONObject)content.get(2)).get("amount"));
	}

	@Test
	public void testAddExpense() throws Exception {
    	Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(DUMMY_USERNAME + "1");

		User user1 = userTestingUtil.createUser(DUMMY_USERNAME + "1", DUMMY_USER_NAME + "1", null, null);
		User user2 = userTestingUtil.createUser(DUMMY_USERNAME + "2", DUMMY_USER_NAME + "2", null, null);
		Group group1 = groupTestingUtil.createGroup(DUMMY_GROUP_NAME + "1", null, null, user1);
		group1.addMember(user2);
		
        JSONObject jo = new JSONObject();
		jo.put(TITLE_FIELD_NAME, DUMMY_EXPENSE_TITLE);
		jo.put(DESCRIPTION_FIELD_NAME, DUMMY_EXPENSE_DESCRIPTION);
		jo.put(IMAGE_FIELD_NAME, DUMMY_IMAGE);
		jo.put(AMOUNT_FIELD_NAME, DUMMY_EXPENSE_AMOUNT);
		jo.put(SHARERS_FIELD_NAME, Arrays.asList(Long.toString(user1.getId()), Long.toString(user2.getId())));
		String testExpenseJson = jo.toString();

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(EXPENSE_ENDPOINT_URL + group1.getId() + "/expense").content(testExpenseJson)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		Assert.assertEquals("wrong response status", 200, response.getStatus());
		Assert.assertEquals("wrong response content", "", response.getContentAsString());
		List<Expense> expenses = Lists.newArrayList(expenseRepository.findAll());
		Assert.assertEquals("wrong number of expenses created", 1, expenses.size());
		List<Expense> groupExpenses = groupRepository.findOne(group1.getId()).getExpenses();
		Assert.assertEquals("wrong number of expenses created for its associated group", 1, groupExpenses.size());
		Expense groupExpense = groupExpenses.get(0);
		Assert.assertEquals("wrong amount set for the group expense", DUMMY_EXPENSE_AMOUNT, groupExpense.getAmount());
		Assert.assertEquals("wrong title set for the group expense", DUMMY_EXPENSE_TITLE, groupExpense.getTitle());
		Assert.assertEquals("wrong description set for the group expense", DUMMY_EXPENSE_DESCRIPTION, groupExpense.getDescription());
		List<Expense> userExpenses = userRepository.findOne(user1.getId()).getExpenses();
		Assert.assertEquals("wrong number of expenses created for its associated group", 1, userExpenses.size());
		Expense userExpense = userExpenses.get(0);
		Assert.assertEquals("wrong expense assigned to a user", DUMMY_EXPENSE_AMOUNT, userExpense.getAmount());
		Assert.assertEquals("wrong expense assigned to a user", DUMMY_EXPENSE_TITLE, userExpense.getTitle());
		Assert.assertEquals("wrong expense assigned to a user", DUMMY_EXPENSE_DESCRIPTION, userExpense.getDescription());
		System.out.println(userRepository.findOne(user2.getId()).getExpenses());
		Assert.assertEquals("wrong expense assigned to a user", DUMMY_EXPENSE_TITLE, userRepository.findOne(user2.getId()).getExpenses().get(0).getTitle());
	}
	
	@Test//TODO fix it here
	public void testDeleteExpense() throws Exception {
    	Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(DUMMY_USERNAME + "1");

		User user1 = userTestingUtil.createUser(DUMMY_USERNAME + "1", DUMMY_USER_NAME + "1", null, null);
		User user2 = userTestingUtil.createUser(DUMMY_USERNAME + "2", DUMMY_USER_NAME + "2", null, null);
		User user3 = userTestingUtil.createUser(DUMMY_USERNAME + "3", DUMMY_USER_NAME + "3", null, null);
		Group group1 = groupTestingUtil.createGroup(DUMMY_GROUP_NAME + "1", null, null, user1);
		Group group2 = groupTestingUtil.createGroup(DUMMY_GROUP_NAME + "2", null, null, user2);
		Expense expense1 = expenseTestingUtil.createExpense(
				DUMMY_EXPENSE_TITLE + "1", DUMMY_EXPENSE_AMOUNT + 1,
				DUMMY_EXPENSE_DESCRIPTION + "1", DUMMY_IMAGE_ID, Arrays.asList(user2.getId(), user3.getId()), user1,
				group1.getId());
		Expense expense2 = expenseTestingUtil.createExpense(
				DUMMY_EXPENSE_TITLE + "2", DUMMY_EXPENSE_AMOUNT + 2,
				DUMMY_EXPENSE_DESCRIPTION + "2", DUMMY_IMAGE_ID, Arrays.asList(user1.getId(), user2.getId(), user3.getId()), user1,
				group1.getId());
		Expense expense3 = expenseTestingUtil.createExpense(
				DUMMY_EXPENSE_TITLE + "3", DUMMY_EXPENSE_AMOUNT + 3,
				DUMMY_EXPENSE_DESCRIPTION + "3", DUMMY_IMAGE_ID, Arrays.asList(user2.getId(), user3.getId()), user1,
				group2.getId());
		
		group1.addMember(user2);
		group1.addMember(user3);
		group2.addMember(user1);
		group2.addMember(user2);
		
		System.out.println(userRepository.findById(user1.getId()).getExpenses());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete(EXPENSE_ENDPOINT_URL + expense1.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		Assert.assertEquals("wrong response status", 200, response.getStatus());
		Assert.assertEquals("wrong response content", "", response.getContentAsString());
		
		List<Expense> userExpenses1 = userRepository.findById(user1.getId()).getExpenses();
		Assert.assertEquals("wrong expnese list size (user1)", 2, userExpenses1.size());
		for (Expense exp: userExpenses1) {
			Assert.assertTrue("deleted expense is still available for a user (user1)", exp.getId() != expense1.getId());
		}
		List<Expense> userExpenses2 = userRepository.findById(user2.getId()).getExpenses();
		Assert.assertEquals("wrong expnese list size (user2)", 2, userExpenses2.size());
		for (Expense exp: userExpenses2) {
			Assert.assertTrue("deleted expense is still available for a user (user2)", exp.getId() != expense1.getId());
		}
		List<Expense> userExpenses3 = userRepository.findById(user3.getId()).getExpenses();
		Assert.assertEquals("wrong expnese list size (user3)", 2, userExpenses3.size());
		for (Expense exp: userExpenses3) {
			Assert.assertTrue("deleted expense is still available for a user (user3)", exp.getId() != expense1.getId());
		}
		//delete group1 and see how many expenses remain (for users)
	}
}
