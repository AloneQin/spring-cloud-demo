package com.example.usercenter.controller;

import com.example.common.response.DefaultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/userInfo")
    public DefaultResponse<String> getUserInfo(String username, HttpServletRequest request) {
        return DefaultResponse.success(request.getRequestURI());
    }

}
