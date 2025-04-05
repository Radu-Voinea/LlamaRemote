package com.crazyllama.llama_remote.server.manager.rest;

import com.crazyllama.llama_remote.server.dto.database.User;
import com.crazyllama.llama_remote.server.dto.database.Workspace;
import com.crazyllama.llama_remote.common.dto.rest.workspace.WorkspaceCreateRequest;
import com.crazyllama.llama_remote.common.dto.rest.workspace.WorkspaceListRequest;
import com.crazyllama.llama_remote.server.manager.DatabaseManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController(value = "/workspace")
public class WorkspaceRestAPI {

	private final DatabaseManager databaseManager;
	private final HashMap<String, User> authenticatedUsers = new HashMap<>();

	public WorkspaceRestAPI(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}

	@GetMapping("/list")
	public ResponseEntity<WorkspaceListRequest.Response> list(@RequestBody WorkspaceListRequest body) {
		User user = User.getByToken(body.token);

		if (user == null) {
			return ResponseEntity.status(401)
					.body(new WorkspaceListRequest.Response("Unauthorised", new ArrayList<>()));
		}

		return ResponseEntity.ok(
				new WorkspaceListRequest.Response(
						"OK",
						Workspace.getByUser(user).stream()
								.map(workspace -> (int) workspace.id)
								.toList()
				)
		);
	}

	@GetMapping("/create")
	public ResponseEntity<WorkspaceCreateRequest.Response> create(@RequestBody WorkspaceCreateRequest body) {
		User user = User.getByToken(body.token);

		if (user == null) {
			return ResponseEntity.status(401)
					.body(new WorkspaceCreateRequest.Response("Unauthorised"));
		}

		Workspace workspace = new Workspace(body.name);

		return ResponseEntity.ok(
				new WorkspaceCreateRequest.Response(
						"OK"
				)
		);
	}


}
