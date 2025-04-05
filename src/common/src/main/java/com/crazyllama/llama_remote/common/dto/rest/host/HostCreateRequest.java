package com.crazyllama.llama_remote.common.dto.rest.host;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class HostCreateRequest {

	public int workspaceId;
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
