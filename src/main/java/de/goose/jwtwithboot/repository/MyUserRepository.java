package de.goose.jwtwithboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.goose.jwtwithboot.domain.MyUser;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {

	MyUser findByUserName(String userName);

}
