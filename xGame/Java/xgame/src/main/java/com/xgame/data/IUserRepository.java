package com.xgame.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xgame.data.entities.User;

public interface IUserRepository extends JpaRepository<User, Integer> {
	List<User> findByNicknameLikeAndIsDeletedFalseOrEmailAndIsDeletedFalse(String nickname, String email);

	User findByEmailAndPasswordHashAndIsDeletedFalse(String email, String passwordHash);
}
