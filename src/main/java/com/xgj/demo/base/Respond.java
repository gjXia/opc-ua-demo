package com.xgj.demo.base;


import lombok.Data;

@Data
public class Respond {

    private int code;
    private String message;
    private Object data;

    private Respond(RespondEnum re) {
        this.code = re.getCode();
        this.message = re.getMessage();
    }


    private Respond(RespondEnum re, Object data) {
        this.code = re.getCode();
        this.message = re.getMessage();
        this.data = data;
    }

    private Respond(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Respond of(RespondEnum re) {
        return new Respond(re);
    }

    public static Respond of(RespondEnum re, Object data) {
        return new Respond(re, data);
    }

    public static Respond of(int code, String message) {
        return new Respond(code, message);
    }

}
