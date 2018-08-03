package com.suda.MyVideoApi.constant;

/**
 * 业务异常错误码
 *
 * @author guhaibo
 * @date 2018/7/22
 */
public enum BizErrorCodeConstants {

    /**
     * 业务异常
     */
    S0000("S0000", "服务器业务异常"),
    S0001("S0001", "数据库异常"),
    S0002("S0002", "解析失败"),
    S0003("S0003", "安全校验失败");

    private String code;

    private String codeMSG;

    BizErrorCodeConstants(String code, String codeMSG) {
        this.code = code;
        this.codeMSG = codeMSG;
    }

    public String getCode() {
        return code;
    }

    public String getCodeMSG() {
        return codeMSG;
    }
}
