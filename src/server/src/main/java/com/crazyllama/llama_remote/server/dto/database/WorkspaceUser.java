package com.crazyllama.llama_remote.server.dto.database;

import com.crazyllama.llama_remote.server.dto.IDatabaseEntry;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity(name = "workspace_users")
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceUser implements IDatabaseEntry<Long> {

	@Id
	@GeneratedValue
	public long id;

	public @ManyToOne User user;
	public @ManyToOne Workspace workspace;

//	public List<String> permissions = new ArrayList<>();

	public WorkspaceUser(Workspace workspace, User user) {
		this.workspace = workspace;
		this.user = user;
	}

	@Override
	public Long getIdentifier() {
		return id;
	}
}
