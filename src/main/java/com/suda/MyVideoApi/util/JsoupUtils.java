package com.suda.MyVideoApi.util;

import com.suda.MyVideoApi.constant.BizErrorCodeConstants;
import com.suda.MyVideoApi.domian.BizException;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Jsoup工具类
 *
 * @author guhaibo
 * @date 2018/7/22
 */
@Log4j2
public class JsoupUtils {
    private static final String UA_PHONE = "Mozilla/5.0 (Linux; Android 4.3; Nexus 10 Build/JSS15Q) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.23 Safari/537.36";
    private static final String UA_PC = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";

    private static final int TIME_OUT = 30 * 1000;

    private static final String ERROR_DESC = "网址请求失败：";

    public static Connection getConnection(String url) {
        return Jsoup.connect(url).userAgent(UA_PC).timeout(TIME_OUT).ignoreContentType(true);
    }

    public static Document getDocWithPC(String url) {
        try {
            return Jsoup.connect(url).userAgent(UA_PC).timeout(TIME_OUT).ignoreContentType(true).get();
        } catch (IOException e) {
            log.error(ERROR_DESC + url);
            throw new BizException(BizErrorCodeConstants.S0002, e);
        }
    }

    public static Document getDocWithPhone(String url) {
        try {
            return Jsoup.connect(url).userAgent(UA_PHONE).timeout(TIME_OUT).ignoreContentType(true).validateTLSCertificates(false).get();
        } catch (IOException e) {
            log.error(ERROR_DESC + url);
            throw new BizException(BizErrorCodeConstants.S0002, e);
        }
    }
}
