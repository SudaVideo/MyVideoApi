package com.suda.MyVideoApi.constant;

/**
 * @author guhaibo
 * @date 2018/7/29
 */
public enum API {



    DILI(0, "https://www.dililitv.com/", "https://www.dililitv.com/gresource/", "https://www.dililitv.com/gresource/%s?Play=%s"),
    MJW(1, "https://91mjw.com/category/", "https://91mjw.com/video/", "https://91mjw.com/video/%s?Play=%s"),
    HJ(2, "https://www.hmtv.me/", "https://www.hmtv.me/show/", "https://www.hmtv.me/show/%s?Play=%s"),
    LZ(3, "http://www.lzvod.net/", "http://www.lzvod.net/", "http://www.lzvod.net/%s?Play=%s");


    public static API getApiBySourceId(int sourceId) {
        for (API api : API.values()) {
            if (api.sourceId == sourceId) {
                return api;
            }
        }
        return DILI;
    }

    public String baseKey;
    public int sourceId;
    public String baseUrl;
    public String resUrl;
    public String resSeriresUrl;

    API(int sourceId, String baseUrl, String resUrl, String resSeriresUrl) {
        this.sourceId = sourceId;
        this.baseUrl = baseUrl;
        this.resUrl = resUrl;
        this.resSeriresUrl = resSeriresUrl;
    }
}
