package com.crazyllama.llama_remote.server.dto.database;

import com.crazyllama.llama_remote.common.dto.rest.host.HostCreateRequest;
import com.crazyllama.llama_remote.server.dto.IDatabaseEntry;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "hosts")
@NoArgsConstructor
@Getter
public class Host implements IDatabaseEntry<Long> {

	@Id
	@GeneratedValue
	private long id;

	private String name;
	private String host;
	private int port;

	private String username;
	private String privateKey;

	public Host(HostCreateRequest request) {
		this.name = request.name;
		this.host = request.host;
		this.port = request.port;
		this.username = request.username;
		this.privateKey = request.privateKey;
	}

	@Override
	public Long getIdentifier() {
		return id;
	}
}
