package com.crazyllama.llama_remote.server.dto.database;

import com.crazyllama.llama_remote.server.dto.IDatabaseEntry;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity(name = "workspace_permissions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WorkspacePermission implements IDatabaseEntry<Long> {

	@Id
	@GeneratedValue
	private long id;

	private @ManyToOne User user;
	private @ManyToOne Host host;

	public WorkspacePermission(User user, Host host) {
		this.user = user;
		this.host = host;
	}

	@Override
	public Long getIdentifier() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		WorkspacePermission that = (WorkspacePermission) o;
		return Objects.equals(user, that.user) && Objects.equals(host, that.host);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(user);
		result = 31 * result + Objects.hashCode(host);
		return result;
	}

	@Override
	public void save() {
		this.user.save();
		this.host.save();
	}
}
