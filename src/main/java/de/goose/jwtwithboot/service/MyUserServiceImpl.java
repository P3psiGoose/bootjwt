package de.goose.jwtwithboot.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.goose.jwtwithboot.domain.MyRole;
import de.goose.jwtwithboot.domain.MyUser;
import de.goose.jwtwithboot.repository.MyRoleRepository;
import de.goose.jwtwithboot.repository.MyUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MyUserServiceImpl implements MyUserService, UserDetailsService {

	private final MyUserRepository userRepository;
	private final MyRoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public MyUser saveUser(MyUser user) {
		log.info("Saving new User to Database {}", user.getName());
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		return userRepository.save(user);
	}

	@Override
	public MyRole saveRole(MyRole role) {
		log.info("Saving new Role to Database {}", role.getName());
		return roleRepository.save(role);
	}

	@Override
	public void addRoleToUser(String userName, String roleName) {
		log.info("Adding the Role {} to User {}", roleName, userName);

		MyUser user = userRepository.findByUserName(userName);
		MyRole role = roleRepository.findByName(roleName);

		user.getRoles().add(role); // No save action because of @Transactional
	}

	@Override
	public MyUser getUser(String userName) {
		log.info("Fetching User from Database {}", userName);
		return userRepository.findByUserName(userName);
	}

	@Override
	public List<MyUser> getAllUsers() {
		log.info("Fetching all users from Database");
		return userRepository.findAll();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MyUser user = userRepository.findByUserName(username);
		if (user == null) {
			log.error("User not found: {}", username);
		} else {
			log.info("User found: {}", username);
		}

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().stream().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

		return new User(user.getUserName(), user.getPassword(), authorities);
	}

}
