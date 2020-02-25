package com.dongnaoedu.mall.miaosha;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.dongnaoedu.mall.miaosha.schedule.LoadItemScheduler;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MallMiaoshaApplication.class)
public class MallMiaoshaApplicationTests {
    // -------开始 构建spring web mvc 环境

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    // ------- 结束 构建spring web mvc 环境

    @Test
    public void miaoshaTest1() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/order/miaosha");
        String responseContent = mockMvc.perform(request).andReturn().getResponse()
                .getContentAsString();
        System.out.println(" 完成一次请求,结果：" + responseContent);
    }

    @Autowired
    LoadItemScheduler loadItemScheduler;

    @Test
    public void miaoshaLoad() throws Exception {
        loadItemScheduler.execute("");
    }

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void rabbitmq() throws Exception {
        long t = System.currentTimeMillis();
        for (int i = 0; i < 50000; i++) {
            rabbitTemplate.convertAndSend("test-ha-ex", "",
                    "10241024001024102400102410240010241024001024102400102410240010241024001024102400102410240010241024001024102400102410240010241024");
        }
        System.out.println("发送成功:" + (System.currentTimeMillis() - t));
        Thread.sleep(3000L);
    }

}
