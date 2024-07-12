package com.nhnacademy.bookstore.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.support.HttpHeaders;

@Configuration
public class ElasticSearchConfig {

	@Value("${spring.elasticsearch.uris}")
	private String[] esHost;

	@Value("${elasticsearch.key}")
	private String key;

	public ClientConfiguration elasticsearchClient() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "ApiKey " + key);

		return ClientConfiguration.builder()
			.connectedTo(esHost)
			.usingSsl()
			.withDefaultHeaders(headers)
			.build();
	}
}