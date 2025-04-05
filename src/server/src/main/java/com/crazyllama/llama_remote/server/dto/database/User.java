package com.crazyllama.llama_remote.server.dto.database;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "users")
public class User {

	@Id
	public String username;
	public String passwordHash;

}
