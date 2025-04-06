package com.crazyllama.llama_remote.server.manager.rest;

import com.crazyllama.llama_remote.common.dto.rest.host.HostCreateRequest;
import com.crazyllama.llama_remote.common.dto.rest.host.HostInfoRequest;
import com.crazyllama.llama_remote.common.dto.rest.host.HostsListRequest;
import com.crazyllama.llama_remote.server.dto.database.Host;
import com.crazyllama.llama_remote.server.dto.database.User;
import com.crazyllama.llama_remote.server.dto.database.Workspace;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HostRestAPI {

	@PostMapping("/host/{workspace_id}/create")
	public ResponseEntity<HostCreateRequest.Response> create(
			@RequestHeader("Authorization") String authHeader,
			@PathVariable("workspace_id") long workspaceId,
			@RequestBody HostCreateRequest body
	) {
		User user = User.getByAuthHeader(authHeader);

		if (user == null) {
			return ResponseEntity
					.status(401)
					.body(new HostCreateRequest.Response("Unauthorized"));
		}

		Workspace workspace = Workspace.getById(workspaceId);

		if (workspace == null) {
			return ResponseEntity
					.status(404)
					.body(new HostCreateRequest.Response("Workspace not found"));
		}

		if (!workspace.hasPermission(user)) {
			return ResponseEntity
					.status(403)
					.body(new HostCreateRequest.Response("Forbidden"));
		}

		workspace.addHost(new Host(body));
		workspace.save();

		return ResponseEntity.ok(new HostCreateRequest.Response("OK"));
	}

	@GetMapping("/host/{workspace_id}/list")
	public ResponseEntity<HostsListRequest.Response> list(
			@RequestHeader("Authorization") String authHeader,
			@PathVariable("workspace_id") long workspaceId
	) {
		User user = User.getByAuthHeader(authHeader);

		if (user == null) {
			return ResponseEntity
					.status(401)
					.body(new HostsListRequest.Response("Unauthorized", new ArrayList<>()));
		}

		Workspace workspace = Workspace.getById(workspaceId);

		if (workspace == null) {
			return ResponseEntity
					.status(404)
					.body(new HostsListRequest.Response("Workspace not found", new ArrayList<>()));
		}

		if (!workspace.hasPermission(user)) {
			return ResponseEntity
					.status(403)
					.body(new HostsListRequest.Response("Forbidden", new ArrayList<>()));
		}

		List<Host> hosts = workspace.getHostsForUser(user);

		return ResponseEntity.ok(new HostsListRequest.Response(
				"OK",
				hosts.stream().map(Host::getIdentifier).toList()
		));
	}

	@GetMapping("/host/{workspace_id}/{host_id}")
	public ResponseEntity<HostInfoRequest.Response> info(
			@RequestHeader("Authorization") String authHeader,
			@PathVariable("workspace_id") long workspaceId,
			@PathVariable("host_id") long hostId
	) {
		User user = User.getByAuthHeader(authHeader);

		if (user == null) {
			return ResponseEntity
					.status(401)
					.body(new HostInfoRequest.Response("Unauthorized", "", "", 0, "", ""));
		}

		Workspace workspace = Workspace.getById(workspaceId);

		if (workspace == null) {
			return ResponseEntity
					.status(404)
					.body(new HostInfoRequest.Response("Workspace not found", "", "", 0, "", ""));
		}

		if (!workspace.hasPermission(user)) {
			return ResponseEntity
					.status(403)
					.body(new HostInfoRequest.Response("Forbidden", "", "", 0, "", ""));
		}

		Host host = workspace.getHostById(hostId);

		return ResponseEntity.ok(new HostInfoRequest.Response(
				"OK",
				host.getName(),
				host.getHost(),
				host.getPort(),
				host.getUsername(),
				host.getPrivateKey()
		));
	}


}
