package org.jetbrains.plugins.template.api;

import com.crazyllama.llama_remote.common.dto.rest.workspace.WorkspaceListRequest;
import lombok.SneakyThrows;
import org.jetbrains.plugins.template.Registry;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

public class WorkspaceAPI {

	@SneakyThrows
	public static List<Integer> getWorkspaces() {
		WorkspaceListRequest workspaceListRequest = new WorkspaceListRequest(Registry.token);

		WorkspaceListRequest.Response response = new APIRequest<>("/workspace/list", workspaceListRequest, WorkspaceListRequest.Response.class)
				.getResponse();

		if (response == null) {
			return new ArrayList<>();
		}

		return response.workspaces;
	}

}
