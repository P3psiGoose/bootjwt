package de.goose.jwtwithboot;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import de.goose.jwtwithboot.domain.MyRole;
import de.goose.jwtwithboot.domain.MyUser;
import de.goose.jwtwithboot.service.MyUserService;

@SpringBootApplication
public class JwtwithbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtwithbootApplication.class, args);
	}

	private static final String ROLE_USER = "ROLE_USER";
	private static final String ROLE_MANAGER = "ROLE_MANAGER";
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	private static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";

	@Bean
	CommandLineRunner run(MyUserService userService) {
		return args -> {
			userService.saveRole(new MyRole(null, ROLE_USER));
			userService.saveRole(new MyRole(null, ROLE_MANAGER));
			userService.saveRole(new MyRole(null, ROLE_ADMIN));
			userService.saveRole(new MyRole(null, ROLE_SUPER_ADMIN));

			userService.saveUser(new MyUser(null, "Donald Trump", "donald", "maga", new ArrayList<>()));
			userService.saveUser(new MyUser(null, "Barak Obama", "barak", "1234", new ArrayList<>()));
			userService.saveUser(new MyUser(null, "George W. Bush", "george", "1234", new ArrayList<>()));
			userService.saveUser(new MyUser(null, "Bill Clinton", "bill", "monica", new ArrayList<>()));

			userService.addRoleToUser("donald", ROLE_USER);
			userService.addRoleToUser("donald", ROLE_ADMIN);

			userService.addRoleToUser("barak", ROLE_MANAGER);

			userService.addRoleToUser("george", ROLE_USER);

			userService.addRoleToUser("bill", ROLE_USER);
			userService.addRoleToUser("bill", ROLE_ADMIN);
			userService.addRoleToUser("bill", ROLE_SUPER_ADMIN);

		};
	}

}
