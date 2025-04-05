package com.crazyllama.llama_remote.server.dto.database;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name = "workspaces")
public class Workspace {

	@Id
	@GeneratedValue
	public long id;

	public String name;

}
