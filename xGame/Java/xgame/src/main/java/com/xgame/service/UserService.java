package com.xgame.service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.common.hash.Hashing;
import com.xgame.common.viewmodels.ProfileViewModel;
import com.xgame.common.viewmodels.UserViewModel;
import com.xgame.data.IUserRepository;
import com.xgame.data.entities.User;
import com.xgame.service.interfaces.IUserService;
import com.xgame.service.models.UserCredentials;

@Service
public class UserService implements IUserService {

	@Autowired
	private IUserRepository userRepo;
	private final String salt = "boys-them-we-team";

	@Override
	public UserViewModel registerNewUser(UserCredentials credentials) {
		var hashedPassword = Hashing.sha256().hashString(credentials.password + salt, StandardCharsets.UTF_8)
				.toString();
		var user = new User(credentials.nickname, credentials.email, hashedPassword);
		userRepo.save(user);

		return new UserViewModel(user);
	}

	@Override
	public List<UserViewModel> search(String param) {
		var users = userRepo.findByNicknameLikeAndIsDeletedFalseOrEmailAndIsDeletedFalse("%" + param + "%", param);

		return users.stream().map(u -> new UserViewModel(u)).collect(Collectors.toList());
	}

	@Override
	public UserViewModel login(UserCredentials credentials) throws ResponseStatusException {
		var hashedPassword = Hashing.sha256().hashString(credentials.password + salt, StandardCharsets.UTF_8)
				.toString();
		var user = userRepo.findByEmailAndPasswordHashAndIsDeletedFalse(credentials.email, hashedPassword);

		if (user == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No account found for given email and password.");
		}

		return new UserViewModel(user);

	}

	@Override
	public Boolean deactivateUser(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deactivateUser(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProfileViewModel getProfile(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProfileViewModel getProfile(String email) {
		// TODO Auto-generated method stub
		return null;
	}
}
