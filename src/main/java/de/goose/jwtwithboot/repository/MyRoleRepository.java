package de.goose.jwtwithboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.goose.jwtwithboot.domain.MyRole;

public interface MyRoleRepository extends JpaRepository<MyRole, Long> {
	
	MyRole findByName(String name);
}
