package de.goose.jwtwithboot.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.goose.jwtwithboot.domain.MyRole;
import de.goose.jwtwithboot.domain.MyUser;
import de.goose.jwtwithboot.service.MyUserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

	private final MyUserService userService;

	@GetMapping("/users")
	public ResponseEntity<List<MyUser>> getUsers() {
		return ResponseEntity.ok().body(userService.getAllUsers());
	}

	@PostMapping("/user/save")
	public ResponseEntity<MyUser> saveUser(@RequestBody MyUser user) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/user/save").toUriString());
		return ResponseEntity.created(uri).body(userService.saveUser(user));
	}

	@PostMapping("/role/save")
	public ResponseEntity<MyRole> saveRole(@RequestBody MyRole role) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/role/save").toUriString());
		return ResponseEntity.created(uri).body(userService.saveRole(role));
	}

	@PostMapping("/role/addtouser")
	public ResponseEntity<Void> addRoleToUser(RoleToUserForm form) {
		userService.addRoleToUser(form.getRoleName(), form.getUserName());
		return ResponseEntity.ok().build();
	}
}

@Data
class RoleToUserForm {
	private String userName;
	private String roleName;
}
