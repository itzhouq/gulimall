package com.atguigu.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author itzhouq
 * @date 2020/8/24 21:54
 */

@Configuration
@EnableDiscoveryClient
public class GulimallElasticSearchConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost("47.96.30.109", 9200, "http")));
    }
}
