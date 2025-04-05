package com.crazyllama.llama_remote.common.dto.rest.host;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

public class HostInfoRequest {

	@AllArgsConstructor
	@NoArgsConstructor
	public static class Response {

		public String response;
		public String name;
		public String host;
		public int port;

		public String username;
		public String privateKey;

	}

}
