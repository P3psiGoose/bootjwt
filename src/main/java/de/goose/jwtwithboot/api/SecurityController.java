package de.goose.jwtwithboot.api;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.goose.jwtwithboot.domain.MyRole;
import de.goose.jwtwithboot.domain.MyUser;
import de.goose.jwtwithboot.service.MyUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class SecurityController {

	private final MyUserService userService;

	@GetMapping("/token/refresh")
	public void refreshtoken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				// Very redundand CustomAuthenticationFilter and CustomAuthorisationFilter
				String encodedRefreshToken = authorizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); // must come from password vault!

				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT token = verifier.verify(encodedRefreshToken);

				String userName = token.getSubject(); // s. CustomAuthenticationFilter
				MyUser user = userService.getUser(userName);

				String encodedAccessToken = JWT.create()
						.withSubject(user.getUserName())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // + 10 Minutes
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", user.getRoles().stream().map(MyRole::getName).collect(Collectors.toList()))
						.sign(algorithm);

				Map<String, String> tokens = new HashMap<>(); // Or in response body...
				tokens.put("access_token", encodedAccessToken);
				tokens.put("refresh_token", encodedRefreshToken);

				response.setContentType(MediaType.APPLICATION_JSON_VALUE);

				new ObjectMapper().writeValue(response.getOutputStream(), tokens);

			} catch (Exception e) {
				log.error("Errror login in", e);
				response.setHeader("error", e.getMessage());
				// response.sendError(HttpStatus.FORBIDDEN.value()); // too simple

				response.setStatus(HttpStatus.FORBIDDEN.value());
				Map<String, String> error = new HashMap<>();
				error.put("error_message", e.getMessage());

				response.setContentType(MediaType.APPLICATION_JSON_VALUE);

				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}

		} else {
			throw new RuntimeException("Refresh token is missing");
		}

	}

}
