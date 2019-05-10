package com.xdclass.springboot_test.domain;

import java.io.Serializable;

public class JsonData implements Serializable {
    // 状态码 0表示成功，-1表示失败
    private int code;
    // 返回的数据
    private Object data;
    // 错误信息
    private String msg;

    public JsonData(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonData(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
