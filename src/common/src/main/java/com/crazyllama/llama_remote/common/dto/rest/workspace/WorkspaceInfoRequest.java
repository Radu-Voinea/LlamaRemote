package com.crazyllama.llama_remote.common.dto.rest.workspace;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class WorkspaceInfoRequest {

	@AllArgsConstructor
	@NoArgsConstructor
	public static class Response{
		public String response;
		public String name;
	}

}
