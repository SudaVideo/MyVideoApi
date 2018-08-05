package com.suda.MyVideoApi.service;

import com.suda.MyVideoApi.constant.API;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author guhaibo
 * @date 2018/7/29
 */
@Service("videoMjwServiceImpl")
public class VideoMjwServiceImpl extends BaseVideoService {
    public VideoMjwServiceImpl(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public API getApi() {
        return API.MJW;
    }
}
