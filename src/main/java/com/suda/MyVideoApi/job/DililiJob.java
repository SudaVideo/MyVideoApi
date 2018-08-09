package com.suda.MyVideoApi.job;

import com.suda.MyVideoApi.service.VideoDiliServiceImpl;
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
public class DililiJob extends BaseJob<VideoDiliServiceImpl> {

    public DililiJob(VideoDiliServiceImpl videoService, RedisTemplate redisTemplate) {
        super(videoService, redisTemplate);
    }

    /**
     * 每隔1小时，爬dilili电影
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void startFilm() {
        getVideos("film");
    }

    /**
     * 每隔1小时，爬dilili剧集
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void startTeleplay() {
        getVideos("teleplay");
    }

    /**
     * 每隔1小时，爬dilili动漫
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void startCartoon() {
        getVideos("cartoon");
    }

    /**
     * 每隔1小时，爬dilili综艺
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void startVarietyShow() {
        getVideos("variety_show");
    }

    /**
     * 每隔1小时，爬dilili纪录片
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void startDocumentaryFilm() {
        getVideos("list11");
    }
}
