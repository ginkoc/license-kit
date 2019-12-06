package com.ginko.license.manager.api.restentity;

/**
 * @author ginko
 * @date 8/3/19
 */
public class ResultEntity<T> {

    /**结果码， 不为0则操作失败*/
    private int code;

    /**信息*/
    private String message;

    /**返回的数据，可以为空*/
    private T data;

    public ResultEntity() {
    }

    public ResultEntity(int code, String message) {
        this(code, message, null);
    }

    public ResultEntity(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
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
