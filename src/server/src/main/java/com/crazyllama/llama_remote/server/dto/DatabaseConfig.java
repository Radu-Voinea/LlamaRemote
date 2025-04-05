package com.crazyllama.llama_remote.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseConfig {

	public String connectionURL = "jdbc:mariadb://localhost:8086/llama_remote";
	public String username = "username";
	public String password = "password";
	public String driver = "org.mariadb.jdbc.Driver";
	public String dialect = "org.hibernate.dialect.MariaDBDialect";
	public boolean debug = false;

}
