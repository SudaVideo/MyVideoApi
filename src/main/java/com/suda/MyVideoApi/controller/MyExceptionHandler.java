package com.suda.MyVideoApi.controller;

import com.suda.MyVideoApi.constant.BizErrorCodeConstants;
import com.suda.MyVideoApi.domian.BizException;
import com.suda.MyVideoApi.domian.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 异常处理类
 * @author guhaibo
 * @date 2018/7/22
 */
@ControllerAdvice
public class MyExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.profiles.active}")
    private String env;

    @ExceptionHandler(value = BizException.class)
    public
    @ResponseBody
    ResultDTO MyExceptionHandler(BizException bizException) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setErrorCode(bizException.getErrorCode());
        if ("dev".equals(env)) {
            Writer writer = new StringWriter();
            PrintWriter pw = new PrintWriter(writer);
            bizException.printStackTrace(pw);
            pw.close();
            resultDTO.setErrorMSG(writer.toString());
        } else {
            resultDTO.setErrorMSG(bizException.getErrorMSG());
        }
        logger.error("error", bizException);
        return resultDTO;
    }

    @ExceptionHandler(value = Exception.class)
    public
    @ResponseBody
    ResultDTO MyExceptionHandler(Exception exception) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setErrorCode(BizErrorCodeConstants.S0000.getCode());
        resultDTO.setErrorMSG(BizErrorCodeConstants.S0000.getCodeMSG());
        logger.error("error", exception);
        return resultDTO;
    }
}
