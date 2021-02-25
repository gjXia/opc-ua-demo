package com.xgj.demo.base;


public abstract class GenericController {

    /**
     * 构建有返回的成功响应
     *
     * @param data
     * @return
     */
    protected Respond buildSuccess(Object data) {
        return Respond.of(RespondEnum.SUCCESS, data);
    }

    protected Respond buildSuccess(Object data, String msg) {
        Respond respond = Respond.of(RespondEnum.SUCCESS, data);
        respond.setMessage(msg);
        return respond;
    }

    /**
     * 构建无返回的成功响应
     */
    protected Respond buildSuccess() {
        return Respond.of(RespondEnum.SUCCESS);
    }

    /**
     * 构建失败响应
     */
    protected Respond buildFailure() {
        return Respond.of(RespondEnum.FAILURE);
    }

    protected Respond buildFailure(String msg) {

        Respond respond = Respond.of(RespondEnum.FAILURE);
        respond.setMessage(msg);
        return respond;
    }

    /**
     * 构建指定原因的失败响应
     */
    protected Respond buildFailure(RespondEnum re) {
        return Respond.of(re);
    }

}
