package com.atguigu.gulimall.search;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallSearchApplicationTests {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void contextLoads() {
        System.out.println(restHighLevelClient);
    }

}
