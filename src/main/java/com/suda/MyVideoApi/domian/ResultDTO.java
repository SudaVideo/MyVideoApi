package com.suda.MyVideoApi.domian;

/**
 * 返回结果
 *
 * @author guhaibo
 * @date 2018/7/22
 */
public class ResultDTO<T> {
    private T data;
    private Boolean success;
    protected String errorCode;
    protected String errorMSG;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMSG() {
        return errorMSG;
    }

    public void setErrorMSG(String errorMSG) {
        this.errorMSG = errorMSG;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "data=" + data +
                ", success=" + success +
                ", errorCode='" + errorCode + '\'' +
                ", errorMSG='" + errorMSG + '\'' +
                '}';
    }
}
