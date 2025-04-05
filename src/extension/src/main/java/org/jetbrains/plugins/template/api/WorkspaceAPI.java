package org.jetbrains.plugins.template.api;

import com.crazyllama.llama_remote.common.dto.rest.workspace.WorkspaceInfoRequest;
import com.crazyllama.llama_remote.common.dto.rest.workspace.WorkspaceListRequest;
import org.jetbrains.plugins.template.Registry;

import java.util.ArrayList;
import java.util.List;

public class WorkspaceAPI {

	public static List<Long> getWorkspaces() {
		WorkspaceListRequest.Response response = new APIRequest<>("/workspace/list", "GET", null, WorkspaceListRequest.Response.class).getResponse();

		if (response == null) {
			return new ArrayList<>();
		}

		return response.workspaces;
	}


	public static String getWorkspaceName(long id) {
		WorkspaceInfoRequest.Response response = new APIRequest<>("/workspace/" + id, "GET", null, WorkspaceInfoRequest.Response.class).getResponse();

		if (response == null) {
			return "ERROR LOADING";
		}

		return response.name;
	}


}
