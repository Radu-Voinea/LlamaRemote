package com.crazyllama.llama_remote.server.manager.rest;

import com.crazyllama.llama_remote.common.dto.rest.workspace.*;
import com.crazyllama.llama_remote.server.dto.database.Host;
import com.crazyllama.llama_remote.server.dto.database.User;
import com.crazyllama.llama_remote.server.dto.database.Workspace;
import com.crazyllama.llama_remote.server.dto.database.WorkspacePermission;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

	@PostMapping("/workspace/{id}/add_user")
	public ResponseEntity<WorkspaceAddUserRequest.Response> addUser(
			@RequestHeader("Authorization") String authHeader,
			@PathVariable("id") int id,
			@RequestBody WorkspaceAddUserRequest body
	) {
		User user = User.getByAuthHeader(authHeader);

		if (user == null) {
			return ResponseEntity.status(401)
					.body(new WorkspaceAddUserRequest.Response("Unauthorised"));
		}

		Workspace workspace = Workspace.getById(id);

		if (workspace == null) {
			return ResponseEntity.status(404)
					.body(new WorkspaceAddUserRequest.Response("Workspace not found"));
		}

		if (!workspace.hasPermission(user)) {
			return ResponseEntity.status(403)
					.body(new WorkspaceAddUserRequest.Response("Forbidden"));
		}

		User targetUser = User.getByUsername(body.username);

		if (targetUser == null) {
			return ResponseEntity.status(404)
					.body(new WorkspaceAddUserRequest.Response("User not found"));
		}


		for (Long hostID : body.hostIDs) {
			Host host = workspace.getHostById(hostID);

			if (host == null) {
				continue;
			}

			WorkspacePermission permission = new WorkspacePermission(user, host);
			workspace.addPermission(permission);
		}

		workspace.save();

		return ResponseEntity.ok(
				new WorkspaceAddUserRequest.Response("OK")
		);
	}

	@GetMapping("/workspace/{id}/get_users")
	public ResponseEntity<WorkspaceGetUsersRequest.Response> addUser(
			@RequestHeader("Authorization") String authHeader,
			@PathVariable("id") int id
	) {
		User user = User.getByAuthHeader(authHeader);

		if (user == null) {
			return ResponseEntity.status(401)
					.body(new WorkspaceGetUsersRequest.Response("Unauthorised", new ArrayList<>()));
		}

		Workspace workspace = Workspace.getById(id);

		if (workspace == null) {
			return ResponseEntity.status(404)
					.body(new WorkspaceGetUsersRequest.Response("Workspace not found", new ArrayList<>()));
		}

		if (!workspace.hasPermission(user)) {
			return ResponseEntity.status(403)
					.body(new WorkspaceGetUsersRequest.Response("Forbidden", new ArrayList<>()));
		}


		return ResponseEntity.ok(
				new WorkspaceGetUsersRequest.Response("OK", workspace.getPermissions().stream()
						.map(permission->permission.getUser().username)
						.collect(Collectors.toSet())
						.stream().toList()
				)
		);
	}

}
