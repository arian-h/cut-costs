package com.boot.cut_costs.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.Filter;
import javax.transaction.Transactional;

import org.h2.tools.Server;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.boot.cut_costs.model.CustomUserDetails;
import com.boot.cut_costs.model.Expense;
import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.Invitation;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.ExpenseRepository;
import com.boot.cut_costs.repository.GroupRepository;
import com.boot.cut_costs.repository.InvitationRepository;
import com.boot.cut_costs.repository.UserDetailsRepository;
import com.boot.cut_costs.repository.UserRepository;
import com.boot.cut_costs.service.CustomUserDetailsService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public abstract class BaseControllerTest {
	
	@Autowired
	protected Filter springSecurityFilterChain;
	
	@Autowired 
	protected MockMvc mockMvc;
	
	@MockBean
	protected AuthenticationManager authenticationManager;

	@Autowired
	protected WebApplicationContext context;
	
	@Autowired
	protected CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	protected UserDetailsRepository userDetailsRepository;
	
	@Autowired
	protected GroupRepository groupRepository;
	
	@Autowired
	protected ExpenseRepository expenseRepository;

	@Autowired
	protected InvitationRepository invitationRepository;

	@Autowired
	protected UserRepository userRepository;

	private final static Set<String> generatedUniqueStrings = new HashSet<String>();
	private final static Random randomGenerator = new Random();
	
	protected final static String DUMMY_PASSWORD = "Test1234";
	protected final static String DUMMY_IMAGE_ID = "123456789012345";
	protected final static String DUMMY_IMAGE = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAADklEQVQIW2NgQAXGZHAAGioAza6+Hk0AAAAASUVORK5CYII=";

	protected final static String NAME_FIELD_NAME = "name";	
	protected final static String IMAGE_ID_FIELD_NAME = "imageId";
	protected final static String USERNAME_FIELD_NAME = "username";
	protected final static String PASSWORD_FIELD_NAME = "password";
	protected final static String AMOUNT_FIELD_NAME = "amount";
	protected final static String SHARERS_FIELD_NAME = "sharers";
	protected final static String DESCRIPTION_FIELD_NAME = "description";
	protected final static String IMAGE_FIELD_NAME = "image";
	protected final static String TITLE_FIELD_NAME = "title";
	protected final static String ID_FIELD_NAME = "id";
	protected final static String INVITEE_ID_FIELD_NAME = "inviteeId";
	protected final static String GROUP_ID_FIELD_NAME = "groupId";
	
	protected final static String EXPENSE_ENDPOINT_URL = "/expense/";
	protected final static String GROUP_ENDPOINT_URL = "/group/";
	protected final static String USER_ENDPOINT_URL = "/user/";
	protected final static String SIGNUP_ENDPOINT = "/auth/signup";
	protected final static String INVITATION_ENDPOINT = "/invitation/";

	@BeforeClass
	public static void init() throws SQLException {
		/*
		 * TODO: Delete the following two lines when going into production. These two lines help developer to 
		 * access the H2 web console through http://localhost:8082/
		 * */
	    Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
	    webServer.start();
	}
	
	@Before
	public void setUp() {
		if (isAuthenticationTest()) {
			mockMvc = MockMvcBuilders.webAppContextSetup(context)
					.addFilters(springSecurityFilterChain).build();
		} else {
			mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		}
	}

	protected String createUniqueEmail() {
		return "test" + createUniqueAlphanumericString(5) + "@test.com";
	}

	protected String[] createUniqueEmail(int arrayLength) {
		String[] uniqueEmailArray = new String[arrayLength];
		for (int index = 0; index < arrayLength; index++) {
			uniqueEmailArray[index] = createUniqueEmail();
		}
		return uniqueEmailArray;
	}

	protected String createUniqueAlphanumericString(int length) {
		StringBuilder strbld = new StringBuilder();
		do {
			strbld.setLength(0);
			for (int index = 0; index < length; index++) {
				char c;
				double d = randomGenerator.nextDouble();
				if (d > 0.5) {
					c = (char) (97 + randomGenerator.nextInt(26));
				} else {
					c = (char) (48 + randomGenerator.nextInt(10));
				}
				strbld.append(c);
			}
		} while (generatedUniqueStrings.contains(strbld.toString()));
		return strbld.toString();
	}
	
	protected String[] createUniqueAlphanumericString(int length, int arrayLength) {
		String[] unqiueAlphanumericStrings = new String[arrayLength];
		for (int index = 0; index < arrayLength; index++) {
			unqiueAlphanumericStrings[index] = this.createUniqueAlphanumericString(length);
		}
		return unqiueAlphanumericStrings;
	}

	protected boolean isAuthenticationTest() {
		return false;
	}
	
	/**
	 * Create a group object and save the it to the db
	 */
	protected Group createGroup(String groupName, String description, String imageId, User admin) {
		Group group = new Group();
		group.setAdmin(admin);
		group.setName(groupName);
		group.setDescription(description);
		group.setImageId(imageId);
		admin.addOwnedGroup(group);
		groupRepository.save(group);
		return group;
	}
	
	/**
	 * Create a group object and save the it to the db
	 */
	protected Expense createExpense(String title, long amount, String description, String imageId, List<Long> sharers, User owner, long groupId) {
		Expense expense = new Expense();
		Group group = groupRepository.findById(groupId);
		expense.setOwner(owner);
		expense.setTitle(title);
		expense.setDescription(description);
		expense.setImageId(imageId);
		expense.setGroup(group);
		expense.setAmount(amount);
		owner.addOwnedExpense(expense);
		List<User> sharerUsers = new ArrayList<User>();
		if (sharers != null && sharers.size() != 0) {
			for (Long sharerId: sharers) {
				if (owner.getId() != sharerId) {
					User sharerUser = userRepository.findById(sharerId);
					sharerUser.addReceivedExpense(expense);
					sharerUsers.add(sharerUser);
				}
			}
		}
		expense.addShareres(sharerUsers);
		group.addExpense(expense);
		expenseRepository.save(expense);
		return expense;
	}
	
	/**
	 * Create a user details object and a user. Map user details to the user and save the user details to the db
	 */
	protected User createUser(String username, String name, String description, String imageId) throws IOException {
		User user = new User();
		user.setName(name);
		user.setDescription(description);
		user.setImageId(imageId);
		CustomUserDetails userDetails = new CustomUserDetails(username, DUMMY_PASSWORD, true, true, true, true, AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
		userDetails.setUser(user);
		userDetailsRepository.save(userDetails);
		return user;
	}
	
	/**
	 * Create an invitation and save it to the db
	 */
	//TODO why does it fail when the order of findByIds change
	protected Invitation createInvitation(long inviterId, long inviteeId, long groupId) {
		Invitation invitation = new Invitation();
		User inviter = userRepository.findById(inviterId);
		User invitee = userRepository.findById(inviteeId);
		Group group = groupRepository.findById(groupId);
		invitation.setGroup(group);
		invitation.setInviter(inviter);
		inviter.addOwnedInvitation(invitation);
		invitation.setInvitee(invitee);
		invitee.addReceivedInvitation(invitation);
		invitationRepository.save(invitation);
		return invitation;
	}
}
