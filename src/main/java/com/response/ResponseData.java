package com.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author JXS
 * @description 数据公共返回类
 **/
@Data
@AllArgsConstructor
public class ResponseData<T> {
    private final static int DEFAULT_SUCCEED_CODE = 0;
    private final static String DEFAULT_SUCCEED_MSG = "ok";

    private final static int DEFAULT_FAILED_CODE = -1;
    private final static String DEFAULT_FAILED_MSG = "error";

    private int code;

    private String msg;

    private T data;

    public static ResponseData succeed() {
        return new ResponseData<>(DEFAULT_SUCCEED_CODE, DEFAULT_SUCCEED_MSG, null);
    }

    public static ResponseData succeed(Object data) {
        return new ResponseData(DEFAULT_SUCCEED_CODE, DEFAULT_SUCCEED_MSG, data);
    }

    public static ResponseData failed() {
        return new ResponseData(DEFAULT_FAILED_CODE, DEFAULT_FAILED_MSG, null);
    }
}
