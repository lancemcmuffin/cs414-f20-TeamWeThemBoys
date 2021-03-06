package com.xgame.restservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.xgame.common.viewmodels.MatchViewModel;
import com.xgame.common.viewmodels.ProfileViewModel;
import com.xgame.data.IChessMatchRepository;
import com.xgame.data.IMessageRepository;
import com.xgame.data.IUserRepository;
import com.xgame.data.entities.User;
import com.xgame.service.interfaces.IChessMatchService;
import com.xgame.service.interfaces.IUserService;
import com.xgame.service.models.UserCredentials;

@SpringBootTest
class UserService {

	@Autowired
	IUserService userService;
	@Autowired
	IUserRepository userRepo;
	@Autowired
	IChessMatchService matchService;
	@Autowired
	IChessMatchRepository matchRepo;
	@Autowired
	IMessageRepository messageRepo;

	@Test
	void search() {
		var user1 = new User("JUnit1", "junit1@email.com", "junit1password");
		var user2 = new User("JUnit2", "junit2@email.com", "junit2password");
		var user3 = new User("JUnit3", "junit3@email.com", "junit3password");
		var userEntity1 = userRepo.save(user1);
		var userEntity2 = userRepo.save(user2);
		var userEntity3 = userRepo.save(user3);

		var fuzzySearch = userService.search("JUnit");
		var exactNicknameSearch = userService.search("JUnit2");
		var emailSearch = userService.search("junit3@email.com");

		var fuzzySearchUserIds = fuzzySearch.stream().map(f -> f.getId()).collect(Collectors.toList());
		var nicknameIds = exactNicknameSearch.stream().map(f -> f.getId()).collect(Collectors.toList());
		var emailIds = emailSearch.stream().map(f -> f.getId()).collect(Collectors.toList());

		assertTrue(fuzzySearch.size() >= 3);
		assertTrue(exactNicknameSearch.size() >= 1);
		assertTrue(emailSearch.size() == 1);

		assertTrue(fuzzySearchUserIds.contains(userEntity1.getId()));
		assertTrue(fuzzySearchUserIds.contains(userEntity2.getId()));
		assertTrue(fuzzySearchUserIds.contains(userEntity3.getId()));
		assertTrue(nicknameIds.contains(userEntity2.getId()));
		assertTrue(emailIds.contains(userEntity3.getId()));

		// cleanup
		userRepo.delete(userEntity1);
		userRepo.delete(userEntity2);
		userRepo.delete(userEntity3);
	}

	@Test
	void registerNewUser_validCredentials() {
		var testNickname = "testNickname1";
		var testEmail = "testEmail1";
		var testPassword = "testPassword1";

		var credentials = new UserCredentials(testNickname, testEmail, testPassword);
		var user = userService.registerNewUser(credentials);

		var search = userService.search("testNickname1");
		var searchedUserNickname = search.stream().map(f -> f.getNickname()).collect(Collectors.toList());

		var loggedInUser = userService.login(credentials);

		assertTrue(search.size() == 1);
		assertTrue(searchedUserNickname.contains(testNickname));

		assertTrue(loggedInUser.getNickname().equals(testNickname));
		assertTrue(loggedInUser.getEmail().equals(testEmail));

		userRepo.deleteById(user.getId());
	}

	@Test
	void registerNewUser_duplicateNickname_throwsException() {
		var testNickname1 = "testNickname1";
		var testEmail1 = "testEmail1";
		var testPassword1 = "testPassword1";

		var testEmail2 = "testEmail2";
		var testPassword2 = "testPassword2";

		var credentials1 = new UserCredentials(testNickname1, testEmail1, testPassword1);
		var credentials2 = new UserCredentials(testNickname1, testEmail2, testPassword2);

		var user = userService.registerNewUser(credentials1);

		Assertions.assertThrows(Exception.class, () -> userService.registerNewUser(credentials2));

		userRepo.deleteById(user.getId());
	}

	@Test
	void registerNewUser_duplicateEmail_throwsException() {
		var testNickname1 = "testNickname1";
		var testEmail1 = "testEmail1";
		var testPassword1 = "testPassword1";

		var testNickname2 = "testNickname2";
		var testPassword2 = "testPassword2";

		var credentials1 = new UserCredentials(testNickname1, testEmail1, testPassword1);
		var credentials2 = new UserCredentials(testNickname2, testEmail1, testPassword2);

		var user = userService.registerNewUser(credentials1);

		Assertions.assertThrows(Exception.class, () -> userService.registerNewUser(credentials2));

		userRepo.deleteById(user.getId());
	}

	@Test
	void login_validCredentials() {
		var testNickname = "testNickname1";
		var testEmail = "testEmail1";
		var testPassword = "testPassword1";

		var credentials = new UserCredentials(testNickname, testEmail, testPassword);
		var user = userService.registerNewUser(credentials);

		var loggedInUser = userService.login(credentials);

		assertTrue(loggedInUser.getNickname().equals(testNickname));
		assertTrue(loggedInUser.getEmail().equals(testEmail));

		userRepo.deleteById(user.getId());
	}

	@Test
	void login_invalidCredentials() {
		var testNickname = "testNickname1";
		var testEmail = "testEmail1";
		var testPassword = "testPassword1";

		var credentials = new UserCredentials(testNickname, testEmail, testPassword);
		var user = userService.registerNewUser(credentials);

		var badCredentials = new UserCredentials("badNickname", "bad@email", "badPassword");

		Assertions.assertThrows(Exception.class, () -> userService.login(badCredentials));

		userRepo.deleteById(user.getId());
	}

	@Test
	void deactivateUser_correctFlag() {
		
		var testNickname = "testNickname1";
		var testEmail = "testEmail1";
		var testPassword = "testPassword1";

		var credentials = new UserCredentials(testNickname, testEmail, testPassword);
		var userView = userService.registerNewUser(credentials);

		var user = userRepo.findById(userView.getId()).get();
		
		try {
			userService.deactivateUser(user.getId());
			user = userRepo.findById(user.getId()).get();
	
			assertTrue(user.getIsDeleted());
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
		userRepo.deleteById(user.getId());
		}
	}
	
	@Test
	void deactivateUser_searchFails() {
		var user1 = new User("JUnit1", "junit1@email.com", "junit1password");
		var user2 = new User("JUnit2", "junit2@email.com", "junit2password");
		var user3 = new User("JUnit3", "junit3@email.com", "junit3password");
		
		var userEntity1 = userRepo.save(user1);
		var userEntity2 = userRepo.save(user2);
		var userEntity3 = userRepo.save(user3);
		try {
		userService.deactivateUser(userEntity2.getId());

		var fuzzySearch = userService.search("JUnit");
		var exactNicknameSearch = userService.search("JUnit2");
		var emailSearch = userService.search("junit3@email.com");

		var fuzzySearchUserIds = fuzzySearch.stream().map(f -> f.getId()).collect(Collectors.toList());
		var nicknameIds = exactNicknameSearch.stream().map(f -> f.getId()).collect(Collectors.toList());
		var emailIds = emailSearch.stream().map(f -> f.getId()).collect(Collectors.toList());

		assertTrue(fuzzySearch.size() >= 2);
		assertTrue(exactNicknameSearch.size() >= 0);
		assertTrue(emailSearch.size() == 1);

		assertTrue(fuzzySearchUserIds.contains(userEntity1.getId()));
		assertTrue(fuzzySearchUserIds.contains(userEntity3.getId()));
		assertTrue(emailIds.contains(userEntity3.getId()));
		
		assertTrue(!fuzzySearchUserIds.contains(userEntity2.getId()));
		assertTrue(!nicknameIds.contains(userEntity2.getId()));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		// cleanup
		finally {
			userRepo.delete(userEntity1);
			userRepo.delete(userEntity2);
			userRepo.delete(userEntity3);
		}
	}
	
	@Test
	void deactivateUser_cannotLogin() {
		var testNickname = "testNickname1";
		var testEmail = "testEmail1";
		var testPassword = "testPassword1";
		var credentials = new UserCredentials(testNickname, testEmail, testPassword);
		var user = userService.registerNewUser(credentials);
		try {
			userService.deactivateUser(user.getId());
			Assertions.assertThrows(Exception.class, () -> userService.login(credentials));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			userRepo.deleteById(user.getId());
		}
	}
	
	@Test
	void getProfileViewModel() {
		User player1 = null;
		User player2 = null;
		ProfileViewModel player1Profile = null;
		MatchViewModel match = null;
		
		try {
			player1 = userRepo.save(new User("junit1", "junit1@email.com", "junit1password"));
			player2 = userRepo.save(new User("junit2", "junit2@email.com", "junit2password"));
			
			
			match = matchService.createMatch(player1.getId(), player2.getId());
			matchService.acceptInvite(match.getId());
			matchService.suggestDraw(match.getId(), player1.getId());
			matchService.suggestDraw(match.getId(), player2.getId());
			player1Profile = userService.getProfile(player1.getId());
			
			assertNotNull(player1Profile);
			assertTrue(player1.getNickname().equals(player1Profile.getUser()));
			assertTrue(player1Profile.getMatchHistory().size() == 1);
		}
		catch(Exception e) {
			fail(e);
		}
		finally {
			//cleanup
			if(match != null) {
				matchRepo.deleteById(match.getId());
			}
			if(player1 != null) {
				var messages = messageRepo.findByUserIdAndReadTimestampIsNull(player1.getId());
				messageRepo.deleteAll(messages);
				userRepo.delete(player1);
			}
			if(player2 != null) {
				var messages2 = messageRepo.findByUserIdAndReadTimestampIsNull(player2.getId());
				messageRepo.deleteAll(messages2);
				userRepo.delete(player2);
			}
		}
	}
}
