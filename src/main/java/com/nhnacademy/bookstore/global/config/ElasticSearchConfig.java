package com.nhnacademy.bookstore.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.elasticsearch.ReactiveElasticsearchClientAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchClients;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchConfiguration;

import java.time.Duration;

@Configuration
public class ElasticSearchConfig extends ReactiveElasticsearchConfiguration {
    @Override
    public ClientConfiguration clientConfiguration(){
        return ClientConfiguration.builder()
                .connectedTo("115.94.72.198:9200")
                .usingSsl()
                .withConnectTimeout(Duration.ofSeconds(5))
                .withSocketTimeout(Duration.ofSeconds(3))
                //.withBasicAuth(username, password)
                .build();
    }
}
