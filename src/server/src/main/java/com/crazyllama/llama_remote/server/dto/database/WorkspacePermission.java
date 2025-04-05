package com.crazyllama.llama_remote.server.dto.database;

import com.crazyllama.llama_remote.server.dto.IDatabaseEntry;
import com.crazyllama.llama_remote.server.manager.DatabaseManager;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.List;

@Entity(name = "workspace_users")
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
}
