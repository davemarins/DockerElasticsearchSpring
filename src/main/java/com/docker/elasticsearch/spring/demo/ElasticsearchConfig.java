package com.docker.elasticsearch.spring.demo;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.docker.elasticsearch.spring.demo.repository")
@ComponentScan(basePackages = { "com.docker.elasticsearch.spring.demo.service" })
public class ElasticsearchConfig {

    @Value("${elasticsearch.home:/usr/local/Cellar/elasticsearch/6.2.2}")
    private String elasticsearchHome;

    @Value("${elasticsearch.cluster.name:elasticsearch}")
    private String clusterName;

    @Bean
    public Client client() {
        try {
            Settings elasticsearchSettings = Settings.builder()
                    .put("client.transport.sniff", true)
                    .put("path.home", elasticsearchHome)
                    .put("cluster.name", clusterName).build();
            TransportClient client = new PreBuiltTransportClient(elasticsearchSettings);
            client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9200));
            return client;
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
            return null;
        }
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(client());
    }
}