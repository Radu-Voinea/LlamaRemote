package com.crazyllama.llama_remote.server.dto.rest.auth;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

	public String username;
	public String passwordHash;

	@AllArgsConstructor
	@NoArgsConstructor
	public static class Response {

		public String response;
		public String token;

	}

}