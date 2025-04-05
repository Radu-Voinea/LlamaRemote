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
	}

}
