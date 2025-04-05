package com.crazyllama.llama_remote.server.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class Auth {

	@AllArgsConstructor
	@NoArgsConstructor
	public static class Request {
		public String username;
		public String passwordHash;
	}
	
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Response {
		public String response;
		public String token;

		public static String generateToken() {
			String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
			StringBuilder token = new StringBuilder();

			for (int i = 0; i < 128; i++) {
				int index = (int) (Math.random() * characters.length());
				token.append(characters.charAt(index));
			}

			return token.toString();
		}
	}

}
