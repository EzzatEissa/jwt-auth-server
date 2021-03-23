package com.sbm;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.sbm.modules.consent.repository")

@EntityScan({ "com.sbm" })
public class JwtAuthServerApplication {

	public static void main(String... args) {
		SpringApplication.run(JwtAuthServerApplication.class, args);
	}

	@Bean
	org.h2.tools.Server h2Server() {
		Server server = new Server();
		try {
			server.runTool("-tcp");
			server.runTool("-tcpAllowOthers");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return server;

	}

}
