package com.example.common.response;

import com.example.common.utils.JacksonUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 自定义默认返回结构（JSON）
 * @param <T> 内容主体泛型
 */
@Getter
@Setter
public class DefaultResponse<T> {

    /**
     * 返回码
     */
    private String code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 内容主体
     */
    private T content;

    /**
     * 跟踪标识
     */
    private String traceId;

    private DefaultResponse(String code, String message, T content) {
        this.code = code;
        this.message = message;
        this.content = content;
    }

    public static <T> DefaultResponse<T> success(T content) {
        return new DefaultResponse<>(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage(), content);
    }

    public static <T> DefaultResponse<T> success() {
        return success(null);
    }

    public static <T> DefaultResponse<T> fail(String code, String message, T content) {
        return new DefaultResponse<>(code, message, content);
    }

    public static <T> DefaultResponse<T> fail(ReturnCode returnCode, T content) {
        return fail(returnCode.getCode(), returnCode.getMessage(), content);
    }

    public static <T> DefaultResponse<T> fail(ReturnCode returnCode) {
        return fail(returnCode, null);
    }

    public static <T> DefaultResponse<T> fail() {
        return fail(ReturnCodeEnum.FAIL);
    }

    /**
     * 获取详细信息
     * @return 详细信息
     */
    public String detailMessage() {
        if (successful(this)) {
            return message;
        }
        if (Objects.isNull(content)) {
           return message;
        }
        return message + ": " + JacksonUtils.toString(content);
    }

    /**
     * 检查响应信息是否是{@code "执行成功"}
     * @param defaultResponse 目标响应信息
     * @return {@code true}=是，{@code false}=否
     */
    public static boolean successful(DefaultResponse defaultResponse) {
        if (defaultResponse != null && ReturnCodeEnum.SUCCESS.getCode().equals(defaultResponse.getCode())) {
            return true;
        }
        return false;
    }
}
