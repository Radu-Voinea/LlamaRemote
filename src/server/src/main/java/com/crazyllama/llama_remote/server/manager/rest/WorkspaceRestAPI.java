package com.crazyllama.llama_remote.server.manager.rest;

import com.crazyllama.llama_remote.server.dto.database.User;
import com.crazyllama.llama_remote.server.dto.rest.workspace.WorkspaceListRequest;
import com.crazyllama.llama_remote.server.manager.DatabaseManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
		return null;
	}


}
