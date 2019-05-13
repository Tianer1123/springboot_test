package com.xdclass.springboot_test;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringbootTestApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testArgsController() throws Exception{
        // perform模拟一个http请求
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1?arg1=1&arg2=2")).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}
