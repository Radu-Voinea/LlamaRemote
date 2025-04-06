package com.crazyllama.llama_remote.common.dto.rest.workspace;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

public class WorkspaceGetUsersRequest {

	@AllArgsConstructor
	@NoArgsConstructor
	public static class Response {
		public String response;
		public List<String> usernames;
	}

}
