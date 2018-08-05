package com.suda.MyVideoApi.job;

import com.suda.MyVideoApi.service.VideoHJServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author guhaibo
 * @date 2018/7/29
 */
@Component
@Log4j2
public class HJJob extends BaseJob<VideoHJServiceImpl> {

    public HJJob(VideoHJServiceImpl videoService, RedisTemplate redisTemplate) {
        super(videoService, redisTemplate);
    }

    /**
     * 每隔1小时，爬韩剧
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void startFilm() {
        getVideos("hanju");
    }

}
