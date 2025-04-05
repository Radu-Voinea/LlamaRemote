package com.crazyllama.llama_remote.server.manager.rest;

import com.crazyllama.llama_remote.common.dto.rest.workspace.WorkspaceCreateRequest;
import com.crazyllama.llama_remote.common.dto.rest.workspace.WorkspaceInfoRequest;
import com.crazyllama.llama_remote.common.dto.rest.workspace.WorkspaceListRequest;
import com.crazyllama.llama_remote.server.dto.database.User;
import com.crazyllama.llama_remote.server.dto.database.Workspace;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WorkspaceRestAPI {

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

		List<Workspace> workspaces = Workspace.getByUser(user);

		return ResponseEntity.ok(
				new WorkspaceListRequest.Response(
						"OK",
						workspaces.stream()
								.map(Workspace::getIdentifier)
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

		Workspace workspace = new Workspace(body.name, user);
		workspace.save();

		return ResponseEntity.ok(
				new WorkspaceCreateRequest.Response("OK")
		);
	}


	@GetMapping("/workspace/{id}")
	public ResponseEntity<WorkspaceInfoRequest.Response> create(
			@RequestHeader("Authorization") String authHeader,
			@PathVariable("id") int id
	) {
		User user = User.getByAuthHeader(authHeader);

		if (user == null) {
			return ResponseEntity.status(401)
					.body(new WorkspaceInfoRequest.Response("Unauthorised", ""));
		}

		Workspace workspace = Workspace.getById(id);

		if (workspace == null) {
			return ResponseEntity.status(404)
					.body(new WorkspaceInfoRequest.Response("Workspace not found", ""));
		}

		if (!workspace.hasPermission(user)) {
			return ResponseEntity.status(403)
					.body(new WorkspaceInfoRequest.Response("Forbidden", ""));
		}

		return ResponseEntity.ok(
				new WorkspaceInfoRequest.Response("OK", workspace.getName())
		);
	}

}
