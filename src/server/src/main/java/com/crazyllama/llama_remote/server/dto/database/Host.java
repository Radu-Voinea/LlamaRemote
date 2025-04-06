package com.crazyllama.llama_remote.server.dto.database;

import com.crazyllama.llama_remote.common.dto.rest.host.HostCreateRequest;
import com.crazyllama.llama_remote.server.dto.IDatabaseEntry;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		Host host1 = (Host) o;
		return port == host1.port && Objects.equals(name, host1.name) && Objects.equals(host, host1.host) && Objects.equals(username, host1.username) && Objects.equals(privateKey, host1.privateKey);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(name);
		result = 31 * result + Objects.hashCode(host);
		result = 31 * result + port;
		result = 31 * result + Objects.hashCode(username);
		result = 31 * result + Objects.hashCode(privateKey);
		return result;
	}
}
