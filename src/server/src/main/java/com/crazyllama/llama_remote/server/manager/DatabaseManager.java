package com.crazyllama.llama_remote.server.manager;

import com.crazyllama.llama_remote.server.dto.DatabaseConfig;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.SchemaToolingSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

@Getter
public class DatabaseManager {

	@Getter
	@Accessors(fluent = true)
	private static DatabaseManager instance;

	private SessionFactory sessionFactory;

	public DatabaseManager(@NotNull DatabaseConfig config) {
		connect(config);
		instance = this;
	}

	public void connect(DatabaseConfig config) {
		System.out.println("[DatabaseManager] Connecting to database " + config.getConnectionURL() + ". Username: " + config.getUsername());
		Configuration configuration = new Configuration();

		// Register entities

		configuration.setProperty(JdbcSettings.CONNECTION_PROVIDER, "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
		configuration.setProperty("hibernate.hikari.jdbcUrl", config.getConnectionURL());
		configuration.setProperty("hibernate.hikari.username", config.getUsername());
		configuration.setProperty("hibernate.hikari.password", config.getPassword());
		configuration.setProperty("hibernate.hikari.driverClassName", config.getDriver());
		configuration.setProperty(JdbcSettings.DIALECT, config.dialect);
		configuration.setProperty(JdbcSettings.AUTOCOMMIT, Boolean.TRUE.toString());
		configuration.setProperty(SchemaToolingSettings.HBM2DDL_AUTO, "update");
		configuration.setProperty(JdbcSettings.SHOW_SQL, String.valueOf(config.isDebug()));
		configuration.setProperty(JdbcSettings.FORMAT_SQL, String.valueOf(config.isDebug()));
		configuration.setProperty(JdbcSettings.HIGHLIGHT_SQL, String.valueOf(config.isDebug()));

		sessionFactory = configuration.buildSessionFactory();
	}
}
