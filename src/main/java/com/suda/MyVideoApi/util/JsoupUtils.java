package com.suda.MyVideoApi.util;

import com.alibaba.fastjson.JSON;
import com.suda.MyVideoApi.constant.BizErrorCodeConstants;
import com.suda.MyVideoApi.domian.BizException;
import lombok.extern.log4j.Log4j2;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    static OkHttpClient okHttpClient = new OkHttpClient();

    /**
     * 上一次代理刷新时间
     */
    static long lastProxyTime = 0;

    /**
     * 代理池
     */
    static List<HttpProxy> httpProxies = new ArrayList<>();

    public static Connection getConnection(String url) {
        return Jsoup.connect(url).userAgent(UA_PC).timeout(TIME_OUT).ignoreContentType(true);
    }

    public static Document getDocWithPC(String url) {
        int tryTime = 5;
        while (tryTime > 0) {
            try {
                HttpProxy httpProxy = getProxy();
                if (httpProxy != null) {
                    System.setProperty("http.proxySet", "true");
                    System.getProperties().put("http.proxyHost", httpProxy.host);
                    System.getProperties().put("http.proxyPort", httpProxy.port);
                } else {
                    System.setProperty("http.proxySet", "false");
                }

                Document document = Jsoup.connect(url).userAgent(UA_PC).timeout(TIME_OUT)
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true).get();
                tryTime = 0;
                return document;
            } catch (IOException e) {
                tryTime--;
                log.error(ERROR_DESC + url);
            }
        }
        return null;
    }

    public static Document getDocWithPhone(String url) {
        try {
            return Jsoup.connect(url).userAgent(UA_PHONE).timeout(TIME_OUT).ignoreContentType(true).validateTLSCertificates(false).get();
        } catch (IOException e) {
            log.error(ERROR_DESC + url);
            throw new BizException(BizErrorCodeConstants.S0002, e);
        }
    }


    static class HttpProxy {
        public String host;
        public String port;

        @Override
        public String toString() {
            return "HttpProxy{" +
                    "host='" + host + '\'' +
                    ", port='" + port + '\'' +
                    '}';
        }
    }

    /**
     * 获取代理
     *
     * @return
     */
    private synchronized static HttpProxy getProxy() {
        try {

            //每10分钟刷新代理池
            if (System.currentTimeMillis() - lastProxyTime > 60 * 10 * 1000) {
                httpProxies.clear();
            }

            if (httpProxies.size() <= 0) {
                Request request = new Request.Builder().url("https://raw.githubusercontent.com/fate0/proxylist/master/proxy.list").get().build();
                Call call = okHttpClient.newCall(request);
                String result = call.execute().body().string();

                for (String proxy : result.split("\n")) {
                    if (httpProxies.size() > 10) {
                        break;
                    }
                    httpProxies.add(JSON.parseObject(proxy, HttpProxy.class));
                }
                lastProxyTime = System.currentTimeMillis();
            }

            Collections.shuffle(httpProxies);
            return httpProxies.get(0);
        } catch (Exception e) {
            return null;
        }
    }

}
