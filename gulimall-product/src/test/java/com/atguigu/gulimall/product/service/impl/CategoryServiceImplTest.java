package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author itzhouq
 * @date 2020/8/9 16:05
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void findCatelogPath() {
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("catelogPath: {}", Arrays.asList(catelogPath));
    }
}