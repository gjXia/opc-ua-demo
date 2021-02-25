package com.xgj.demo.base;

import lombok.Getter;

public enum RespondEnum {

    SUCCESS(200, "success"),
    FAILURE(10000, "失败"),
    SYSTEM_ERROR(500, "系统错误"),
    PARAMETER_ERROR(10001, "参数验证错误"),
    EXISTED(10002, "已经存在"),
    PARAMETER_BLANK(10003, "参数为空"),
    STATUS_ERROR(10004, "状态异常"),
    QTY_ERROR(10005, "数量异常"),
    EXISTED_ORDER_WORD(10006, "工单已经存在"),
    LOGIN_INVALID(20000, "登陆失效"),
    ;

    @Getter
    private int code;

    @Getter
    private String message;

    private RespondEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
