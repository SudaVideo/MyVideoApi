package com.suda.MyVideoApi.domian;


import com.suda.MyVideoApi.constant.BizErrorCodeConstants;

/**
 * @author guhaibo
 * @date 2018/7/22
 */

public class BizException extends RuntimeException {

    public BizException(BizErrorCodeConstants error, Throwable ex) {
        super(error.getCodeMSG(), ex);
        this.errorCode = error.getCode();
        this.errorMSG = error.getCodeMSG();
        this.ex = ex;
    }

    public BizException(BizErrorCodeConstants error, String errorWhere, Throwable ex) {
        super(error.getCodeMSG() + " " + errorWhere, ex);
        this.errorCode = error.getCode();
        this.errorMSG = error.getCodeMSG() + " " + errorWhere;
        this.ex = ex;
    }

    public BizException(Throwable ex) {
        super(ex);
        this.ex = ex;
    }

    protected String errorCode;
    protected String errorMSG;

    protected Throwable ex;


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

    public Throwable getEx() {
        return ex;
    }

    public void setEx(Throwable ex) {
        this.ex = ex;
    }

}
