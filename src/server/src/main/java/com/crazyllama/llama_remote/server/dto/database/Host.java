package com.crazyllama.llama_remote.server.dto.database;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name = "hosts")
public class Host {

	@Id
	@GeneratedValue
	public long id;

	public String name;
	public String ip;
	public int port;

	public String username;
	public String privateKey;

}
