package com.suda.MyVideoApi.service;

import com.suda.MyVideoApi.constant.API;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author guhaibo
 * @date 2018/7/29
 */
@Service("videoDiliServiceImpl")
public class VideoDiliServiceImpl extends BaseVideoService {

    public VideoDiliServiceImpl(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public API getApi() {
        return API.DILI;
    }
}
