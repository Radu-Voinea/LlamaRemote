package com.crazyllama.llama_remote.server.dto.database;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.ArrayList;
import java.util.List;

@Entity
public class WorkspaceUser {

	@Id
	@GeneratedValue
	public long id;

	public @ManyToOne User user;
	public @ManyToOne Workspace workspace;

	public List<String> permissions = new ArrayList<>();

}
