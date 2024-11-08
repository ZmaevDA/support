package ru.zmaev.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponseInterceptor;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchClients;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

@Slf4j
@Configuration
@EnableElasticsearchRepositories(basePackages = "ru.zmaev.repository.elasticsearch")
@ComponentScan(basePackages = "ru.zmaev")
public class ElasticsearchConfig {

    @Value("${elasticsearch.host}")
    private String host;

    @Bean
    public RestClient elasticsearchRestClient() {
        return RestClient.builder(new HttpHost(host, 9200, "http"))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.addInterceptorLast((HttpResponseInterceptor) (response, context) ->
                            response.addHeader("X-Elastic-Product", "Elasticsearch"));
                    return httpClientBuilder;
                })
                .build();
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(RestClient restClient) {
        return ElasticsearchClients.createImperative(restClient);
    }

    @Bean(name = { "elasticsearchOperations", "elasticsearchTemplate" })
    public ElasticsearchOperations elasticsearchOperations(
            ElasticsearchClient elasticsearchClient) {

        ElasticsearchTemplate template = new ElasticsearchTemplate(elasticsearchClient);
        template.setRefreshPolicy(null);

        return template;
    }
}
