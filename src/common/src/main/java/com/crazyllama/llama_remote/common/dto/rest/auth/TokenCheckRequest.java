package com.crazyllama.llama_remote.common.dto.rest.auth;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class TokenCheckRequest {

	@AllArgsConstructor
	@NoArgsConstructor
	public static class Response{

		public String response;
		public boolean status;

	}

}
