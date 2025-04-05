package com.crazyllama.llama_remote.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Server {

	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
	}

}
