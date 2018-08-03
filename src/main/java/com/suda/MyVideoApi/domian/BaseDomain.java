package com.suda.MyVideoApi.domian;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @author guhaibo
 * @date 2018/7/22
 */
public class BaseDomain implements Serializable {

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
