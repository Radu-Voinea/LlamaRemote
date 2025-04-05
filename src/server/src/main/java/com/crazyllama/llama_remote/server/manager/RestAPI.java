package com.crazyllama.llama_remote.server.manager;

import com.crazyllama.llama_remote.server.dto.Auth;
import com.crazyllama.llama_remote.server.dto.database.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class RestAPI {

	private final DatabaseManager databaseManager;
	private final HashMap<String, User> authenticatedUsers = new HashMap<>();

	public RestAPI(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}

	@GetMapping("/auth")
	public Auth.Response auth(@RequestBody Auth.Request body) {
		User user = User.getByUsername(body.username);

		if (user == null || !user.passwordHash.equals(body.passwordHash)) {
			return new Auth.Response("Invalid credentials", "");
		}

		return new Auth.Response("OK", user.generateToken());
	}

	@GetMapping("/register")
	public Auth.Response register(@RequestBody Auth.Request body) {
		User user = User.getByUsername(body.username);

		if (user != null) {
			return new Auth.Response("User already exists", "");
		}

		user = new User(body);
		user.save();

		return new Auth.Response("OK", user.generateToken());
	}

}
