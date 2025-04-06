package com.crazyllama.llama_remote.common.dto.rest.workspace;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceAddUserRequest {

	public String username;
	public List<Long> hostIDs;

	@AllArgsConstructor
	@NoArgsConstructor
	public static class Response {
		public String response;
	}

}
