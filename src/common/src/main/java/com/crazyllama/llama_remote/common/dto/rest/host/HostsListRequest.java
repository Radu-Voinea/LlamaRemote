package com.crazyllama.llama_remote.common.dto.rest.host;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

public class HostsListRequest {

	@AllArgsConstructor
	@NoArgsConstructor
	public static class Response {

		public String status;
		public List<Long> hosts;

	}

}
