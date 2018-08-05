package com.suda.MyVideoApi.service;

import com.suda.MyVideoApi.constant.API;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author guhaibo
 * @date 2018/7/29
 */
@Service("videoLZServiceImpl")
public class VideoLZServiceImpl extends BaseVideoService {

    public VideoLZServiceImpl(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public API getApi() {
        return API.LZ;
    }
}
