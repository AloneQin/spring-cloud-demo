package com.example.usercenter.openapi;

import com.example.common.response.DefaultResponse;
import com.example.usercenter.controller.UserController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserOpenApi {

    private final UserController userController;

    @GetMapping("/userInfo.open")
    public DefaultResponse<String> getUserInfo(String username, HttpServletRequest request) {
        return userController.getUserInfo(username, request);
    }

}
