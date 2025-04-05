package org.jetbrains.plugins.template.api;

import com.crazyllama.llama_remote.common.dto.rest.workspace.WorkspaceListRequest;
import lombok.SneakyThrows;
import org.jetbrains.plugins.template.MyBundle;
import org.jetbrains.plugins.template.Registry;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class WorkspaceAPI {

	@SneakyThrows
	public static List<Integer> getWorkspaces() {
		WorkspaceListRequest workspaceListRequest = new WorkspaceListRequest(Registry.token);

		HttpRequest request = Registry.createRequest("/workspace/list", workspaceListRequest);

		System.out.println("request: " + request);

		try (HttpClient httpClient = HttpClient.newHttpClient()) {
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			System.out.println("Response: " + response.body());
			WorkspaceListRequest.Response workspaceListResponse = MyBundle.instance().getGson().fromJson(response.body(), WorkspaceListRequest.Response.class);

			return workspaceListResponse.workspaces;
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return new ArrayList<>();
	}

}
