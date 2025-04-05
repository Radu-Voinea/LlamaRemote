package com.crazyllama.llama_remote.server.dto.database;

import com.crazyllama.llama_remote.common.dto.rest.auth.AuthRequest;
import com.crazyllama.llama_remote.server.dto.IDatabaseEntry;
import com.crazyllama.llama_remote.server.manager.DatabaseManager;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.HashMap;

@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User implements IDatabaseEntry<String> {

	public static HashMap<String, User> authenticatedUsers = new HashMap<>();

	@Id
	public String username;
	public String passwordHash;

	public User(AuthRequest request) {
		this.username = request.username;
		this.passwordHash = request.passwordHash;
	}

	public static User getByToken(String token) {
		return authenticatedUsers.get(token);
	}

	public static User getByAuthHeader(String header) {
		if (header == null || !header.startsWith("Bearer ")) {
			return null;
		}

		String token = header.substring(7);
		return User.getByToken(token);
	}

	public static User getByUsername(String username) {
		try (Session session = DatabaseManager.instance().getSessionFactory().openSession()) {
			return session.get(User.class, username);
		} catch (Exception exception) {
			return null;
		}
	}

	public String generateToken() {
		String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder tokenBuilder = new StringBuilder();

		for (int i = 0; i < 64; i++) {
			int index = (int) (Math.random() * characters.length());
			tokenBuilder.append(characters.charAt(index));
		}

		String token = tokenBuilder.toString();

		authenticatedUsers.put(token, this);
		return token;
	}

	@Override
	public String getIdentifier() {
		return username;
	}
}
