// package com.nhnacademy.bookstore.global.config;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.elasticsearch.client.ClientConfiguration;
// import org.springframework.data.elasticsearch.support.HttpHeaders;
//
// @Configuration
// // @EnableElasticsearchRepositories
// public class ElasticSearchConfig {
// 	//
// 	// @Value("${elasticsearch.url}")
// 	// private String elasticHost;
// 	//
// 	// @Value("${elasticsearch.key}")
// 	// private String apiKey;
//
// 	// @Value("${spring.elasticsearch.username}")
// 	// private String username;
// 	//
// 	// @Value("${spring.elasticsearch.password}")
// 	// private String password;
//
// 	@Value("${spring.elasticsearch.uris}")
// 	private String[] esHost;
//
// 	@Value("${elasticsearch.key}")
// 	private String key;
//
// 	public ClientConfiguration elasticsearchClient() {
// 		HttpHeaders headers = new HttpHeaders();
// 		headers.add("Content-Type", "application/json");
// 		headers.add("Authorization", "ApiKey " + key);
//
// 		return ClientConfiguration.builder()
// 			.connectedTo(esHost)
// 			.usingSsl()
// 			// .withConnectTimeout(Duration.ofSeconds(5))
// 			// .withSocketTimeout(Duration.ofSeconds(3))
// 			// .withBasicAuth(username, password)
// 			.withDefaultHeaders(headers)
// 			.build();
// 	}
// }