package com.crazyllama.llama_remote.common.dto.rest.host;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HostCreateRequest {

	public String name;
	public String host;
	public int port;
	public String username;
	public String privateKey;

	@AllArgsConstructor
	@NoArgsConstructor
	public static class Response {

		public String response;

	}

}
