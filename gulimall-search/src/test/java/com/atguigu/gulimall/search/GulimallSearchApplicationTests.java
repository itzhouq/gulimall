package com.atguigu.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.atguigu.gulimall.search.config.GulimallElasticSearchConfig;
import lombok.Builder;
import lombok.Data;
import org.apache.catalina.User;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallSearchApplicationTests {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void indexData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        User user = new User();
        user.setUserName("Jack");
        user.setAge(22);
        user.setGender("男");
        String jsonString = JSON.toJSONString(user);
        // 设置要保存的内容
        indexRequest.source(jsonString, XContentType.JSON);
        // 执行创建索引和保存数据
        IndexResponse index = restHighLevelClient.index(indexRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(index);
    }

    @Data
    class User {
        private String userName;
        private String gender;
        private Integer age;
    }

    @Test
    public void contextLoads() {
        System.out.println(restHighLevelClient);
    }



}
