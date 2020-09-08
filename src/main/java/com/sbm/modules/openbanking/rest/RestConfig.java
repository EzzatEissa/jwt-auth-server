package com.sbm.modules.openbanking.rest;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/api")
public class RestConfig extends ResourceConfig {

	public RestConfig() {
		register(AccountResource.class);
	}
}