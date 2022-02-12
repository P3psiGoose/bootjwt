package de.goose.jwtwithboot.service;

import java.util.List;

import de.goose.jwtwithboot.domain.MyRole;
import de.goose.jwtwithboot.domain.MyUser;

public interface MyUserService {

	MyUser saveUser(MyUser user);

	MyRole saveRole(MyRole role);

	void addRoleToUser(String userName, String roleName); // Username must be unique

	MyUser getUser(String userName);

	List<MyUser> getAllUsers();
}
