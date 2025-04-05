package com.crazyllama.llama_remote.server.dto.database;

import com.crazyllama.llama_remote.server.dto.IDatabaseEntry;
import com.crazyllama.llama_remote.server.manager.DatabaseManager;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "workspaces")
@NoArgsConstructor
@AllArgsConstructor
public class Workspace implements IDatabaseEntry<Long> {

	@Id
	@GeneratedValue
	public long id;

	public String name;

	public Workspace(String name) {
		this.name = name;
	}

	public static List<Workspace> getByUser(User user) {
		try (Session session = DatabaseManager.instance().getSessionFactory().openSession()) {
			return session.createQuery("from workspace_users w where w.user = :user", WorkspaceUser.class)
					.setParameter("user", user)
					.getResultList()
					.stream()
					.map(workspaceUser -> workspaceUser.workspace)
					.toList();
		} catch (Exception exception) {
			return new ArrayList<>();
		}
	}

	@Override
	public Long getIdentifier() {
		return id;
	}
}
