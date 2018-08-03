package com.suda.MyVideoApi.util;

/**
 * @author guhaibo
 * @date 2018/7/29
 */
public class TextUtil {

    /**
     * 字符串是否为空
     *
     * @param content
     * @return
     */
    public static boolean isStrEmpty(String content) {
        return content == null || "".equals(content);
    }
}
