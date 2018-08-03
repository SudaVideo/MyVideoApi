package com.suda.MyVideoApi.domian;

/**
 * 转换接口
 *
 * @author guhaibo
 * @date 2018/7/31
 */
public interface DataConverter<T, R> {

    /**
     * 转换
     *
     * @param from
     * @param to
     */
    void convert(T from, R to);
}
