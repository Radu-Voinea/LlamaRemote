package com.crazyllama.llama_remote.server.manager;

import com.crazyllama.llama_remote.server.dto.DatabaseConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raduvoinea.utils.generic.dto.Holder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.raduvoinea.utils.file_manager.FileManager;

@Configuration
public class BeanProvider {

	@Bean
	public Gson gson() {
		return new GsonBuilder()
				.disableHtmlEscaping()
				.setPrettyPrinting()
				.create();
	}

	@Bean
	public FileManager fileManager(Gson gson) {
		return new FileManager(Holder.of(gson), "config");
	}

	@Bean
	public DatabaseConfig databaseConfig(FileManager fileManager) {
		return fileManager.load(DatabaseConfig.class);
	}

	@Bean
	public DatabaseManager databaseManager(DatabaseConfig config) {
		return new DatabaseManager(config);
	}

}
