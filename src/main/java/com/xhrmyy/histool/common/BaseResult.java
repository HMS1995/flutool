package com.xhrmyy.histool.common;

/**
 * 统一返回结果类
 * Created by huangshiming on 2017/12/27.
 */
public class BaseResult<T> extends BaseModel {

    /**
     * 状态码：1成功，其他为失败
     */
    public int code = 1;

    /**
     * 成功为success，其他为失败原因
     */
    public String message = "success";

    /**
     * 数据结果集
     */
    public T data;

    public BaseResult() {
    }

    public BaseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
