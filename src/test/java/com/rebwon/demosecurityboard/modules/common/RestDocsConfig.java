package com.rebwon.demosecurityboard.modules.common;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestDocsConfig {

	@Bean
	public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
		return configurer -> configurer.operationPreprocessors()
				.withRequestDefaults(prettyPrint())
				.withResponseDefaults(prettyPrint());
	}
}
