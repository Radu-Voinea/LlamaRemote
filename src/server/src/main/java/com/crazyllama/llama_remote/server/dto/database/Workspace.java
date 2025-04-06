package com.crazyllama.llama_remote.server.dto.database;

import com.crazyllama.llama_remote.server.dto.IDatabaseEntry;
import com.crazyllama.llama_remote.server.manager.DatabaseManager;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "workspaces")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Workspace implements IDatabaseEntry<Long> {

	@Id
	@GeneratedValue
	private long id;

	private String name;
	private @ManyToOne User owner;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Host> hosts;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<WorkspacePermission> permissions;

	public Workspace(String name, User owner) {
		this.name = name;
		this.owner = owner;
		this.hosts = new ArrayList<>();
		this.permissions = new ArrayList<>();
	}

	public static List<Workspace> getByUser(User user) {
		try (Session session = DatabaseManager.instance().getSessionFactory().openSession()) {
			return session.createQuery("SELECT DISTINCT w FROM workspaces w LEFT JOIN w.permissions p WHERE w.owner = :user OR p.user = :user", Workspace.class)
					.setParameter("user", user)
					.getResultList();
		} catch (Exception exception) {
			return new ArrayList<>();
		}
	}

	public static Workspace getById(long id) {
		try (Session session = DatabaseManager.instance().getSessionFactory().openSession()) {
			return session.createQuery("from workspaces w where w.id = :id", Workspace.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (Exception exception) {
			return null;
		}
	}

	public boolean hasPermission(User user) {
		return this.permissions.stream().anyMatch(permission -> permission.getUser().equals(user)) || this.owner.equals(user);
	}

	public List<Host> getHostsForUser(User user) {
		if (user.equals(this.owner)) {
			return this.hosts;
		}

		List<Host> hosts = new ArrayList<>();

		for (WorkspacePermission permission : permissions) {
			if (permission.getUser().equals(user)) {
				hosts.add(permission.getHost());
			}
		}

		return hosts;
	}

	public Host getHostById(long id) {
		for (Host host : hosts) {
			if (host.getId() == id) {
				return host;
			}
		}
		return null;
	}

	public void addHost(Host host) {
		this.hosts.add(host);
	}

	public void addPermission(WorkspacePermission permission) {
		for (WorkspacePermission workspacePermission : permissions) {
			if (permission.equals(workspacePermission)) {
				return;
			}
		}

		this.permissions.add(permission);
	}

	@Override
	public Long getIdentifier() {
		return id;
	}
}
