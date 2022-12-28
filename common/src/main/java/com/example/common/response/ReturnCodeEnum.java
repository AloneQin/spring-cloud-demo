package com.example.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回码枚举
 */
@Getter
@AllArgsConstructor
public enum ReturnCodeEnum implements ReturnCode {

    // ---------------------------通用返回码---------------------------
    SUCCESS                     ("000000", "操作成功"),
    FAIL                        ("000001", "操作失败"),
    PARAM_ERROR                 ("000010", "参数缺省或错误"),
    RESOURCE_NOT_FOUND          ("000404", "资源未找到"),
    METHOD_NOT_ALLOWED          ("000405", "方法不被允许"),
    CLIENT_ERROR                ("0004XX", "客户端请求错误"),
    SERVER_ERROR                ("0005XX", "系统开小差了"),

    // ---------------------------业务返回码---------------------------
    /*
     * 用户相关
     */
    NEED_LOGIN                  ("100000", "需要登录"),
    PERMISSION_DENIED           ("100001", "权限不足"),
    USERNAME_OR_PASSWORD_ERROR  ("100002", "用户名或密码错误"),

    /*
     * 手机相关
     */
    PHONE_ALREADY_EXISTS        ("101000", "手机已存在"),
    ;

    private String code;
    private String message;

}
