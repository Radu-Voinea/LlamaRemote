package com.crazyllama.llama_remote.server.dto.rest.workspace;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceListRequest {

	public String token;

	@AllArgsConstructor
	@NoArgsConstructor
	public static class Response {

		public String response;
		public List<Integer> workspaces;

	}


}