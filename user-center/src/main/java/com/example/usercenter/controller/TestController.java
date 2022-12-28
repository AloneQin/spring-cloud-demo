package com.example.usercenter.controller;

import com.example.common.response.DefaultResponse;
import com.example.common.response.ReturnCodeEnum;
import com.example.common.utils.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/index/{param}")
    public String index(@PathVariable("param") String param, String name) throws InterruptedException {
        Thread.sleep(1000L);
        return param + ":" + name;
    }

    @RequestMapping("/main/{param}")
    public String main(@PathVariable("param") String param) throws InterruptedException {
        if (true) {
            throw new RuntimeException("发生异常啦");
        }
        return param;
    }

    @GetMapping("/exception")
    public void exception() {
        AssertUtils.isNull(1, ReturnCodeEnum.FAIL);
    }

    @GetMapping("/success")
    public DefaultResponse<Void> success() {
        return DefaultResponse.success();
    }

    /**
     * xss 测试
     * @param input 模拟参数，如：<script>alert("xss");</script>
     * @return
     */
    @GetMapping("/testXss")
    public String testXss(String input) {
        log.info("#testXss: {}", input);
        return input;
    }
}
