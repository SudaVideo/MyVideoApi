package com.suda.MyVideoApi.job;

import com.suda.MyVideoApi.service.VideoLZServiceImpl;
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
public class LongzhuJob extends BaseJob<VideoLZServiceImpl> {
    public LongzhuJob(VideoLZServiceImpl videoService, RedisTemplate redisTemplate) {
        super(videoService, redisTemplate);
    }

    /**
     * 每隔1小时，爬lz电影
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void startFilm() {
        getVideos("movies");
    }

    /**
     * 每隔1小时，爬lz剧集
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void startTeleplay() {
        getVideos("teleplay");
    }

    /**
     * 每隔1小时，爬lz动漫
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void startCartoon() {
        getVideos("cartoon");
    }

    /**
     * 每隔1小时，爬lz综艺
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void startVarietyShow() {
        getVideos("variety");
    }

    /**
     * 每隔1小时，爬lz纪录片
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void startDocumentaryFilm() {
        getVideos("fact");
    }
}
