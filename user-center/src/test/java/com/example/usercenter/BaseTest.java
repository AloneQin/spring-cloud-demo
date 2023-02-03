package com.example.usercenter;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseTest {

    static {
        // 设置秘钥
        String password = "spring-cloud-demo";
        // 配置秘钥（项目启动时务必将秘钥设置为启动参数，避免将秘钥直接配置在配置文件中，e.g.: -Djasypt.encryptor.password=spring-cloud-demo）
        System.setProperty("jasypt.encryptor.password", password);
    }

}
