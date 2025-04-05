package com.crazyllama.llama_remote.server.manager.rest;

import com.crazyllama.llama_remote.server.dto.rest.auth.AuthRequest;
import com.crazyllama.llama_remote.server.dto.database.User;
import com.crazyllama.llama_remote.server.manager.DatabaseManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class AuthRestAPI {

	private final DatabaseManager databaseManager;
	private final HashMap<String, User> authenticatedUsers = new HashMap<>();

	public AuthRestAPI(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}

	@GetMapping("/auth")
	public ResponseEntity<AuthRequest.Response> auth(@RequestBody AuthRequest body) {
		User user = User.getByUsername(body.username);

		if (user == null || !user.passwordHash.equals(body.passwordHash)) {
			return ResponseEntity
					.status(401)
					.body(new AuthRequest.Response("Invalid credentials", ""));
		}

		return ResponseEntity.ok(new AuthRequest.Response("OK", user.generateToken()));
	}

	@GetMapping("/register")
	public ResponseEntity<AuthRequest.Response> register(@RequestBody AuthRequest body) {
		User user = User.getByUsername(body.username);

		if (user != null) {
			return ResponseEntity
					.status(401)
					.body(new AuthRequest.Response("User already exists", ""));
		}

		user = new User(body);
		user.save();

		return ResponseEntity.ok(new AuthRequest.Response("OK", user.generateToken()));
	}



}
