package com.crazyllama.llama_remote.server.dto.database;

import com.crazyllama.llama_remote.server.manager.DatabaseManager;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "workspaces")
public class Workspace {

	@Id
	@GeneratedValue
	public long id;

	public String name;

	public Workspace(String name) {
		this.name = name;
	}

	public static List<Workspace> getByUser(User user) {
		try (Session session = DatabaseManager.instance().getSessionFactory().openSession()) {
			return session.createQuery("from workspace_users w where w.user = :user", Workspace.class)
					.setParameter("user", user)
					.getResultList();
		} catch (Exception exception) {
			return new ArrayList<>();
		}
	}

}
