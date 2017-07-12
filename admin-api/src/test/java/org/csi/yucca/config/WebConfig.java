package org.csi.yucca.config;

import org.csi.yucca.adminapi.controller.PublicController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

	@Bean
	public PublicController publicController() {
		return new PublicController();
	}

}
