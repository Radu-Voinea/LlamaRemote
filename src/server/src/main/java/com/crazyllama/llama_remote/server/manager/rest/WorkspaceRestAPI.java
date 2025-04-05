package com.crazyllama.llama_remote.server.manager.rest;

import com.crazyllama.llama_remote.server.dto.database.User;
import com.crazyllama.llama_remote.server.dto.database.Workspace;
import com.crazyllama.llama_remote.common.dto.rest.workspace.WorkspaceCreateRequest;
import com.crazyllama.llama_remote.common.dto.rest.workspace.WorkspaceListRequest;
import com.crazyllama.llama_remote.server.dto.database.WorkspaceUser;
import com.crazyllama.llama_remote.server.manager.DatabaseManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class WorkspaceRestAPI {

	private final DatabaseManager databaseManager;
	private final HashMap<String, User> authenticatedUsers = new HashMap<>();

	public WorkspaceRestAPI(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}

	@GetMapping("/workspace/list")
	public ResponseEntity<WorkspaceListRequest.Response> list(@RequestHeader("Authorization") String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(401)
					.body(new WorkspaceListRequest.Response("Unauthorised", new ArrayList<>()));
		}

		String token = authHeader.substring(7);
		User user = User.getByToken(token);

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


	@PostMapping("/workspace/create")
	public ResponseEntity<WorkspaceCreateRequest.Response> create(
			@RequestHeader("Authorization") String authHeader,
			@RequestBody WorkspaceCreateRequest body) {

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(401)
					.body(new WorkspaceCreateRequest.Response("Unauthorised"));
		}

		String token = authHeader.substring(7);
		User user = User.getByToken(token);

		if (user == null) {
			return ResponseEntity.status(401)
					.body(new WorkspaceCreateRequest.Response("Unauthorised"));
		}

		Workspace workspace = new Workspace(body.name);
		workspace.save();

		WorkspaceUser workspaceUser = new WorkspaceUser(workspace, user);
		workspaceUser.save();

		return ResponseEntity.ok(
				new WorkspaceCreateRequest.Response("OK")
		);
	}

}
