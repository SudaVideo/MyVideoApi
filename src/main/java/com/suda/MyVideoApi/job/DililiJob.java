package com.suda.MyVideoApi.job;

import com.suda.MyVideoApi.domian.dos.VideoDetailDO;
import com.suda.MyVideoApi.domian.dto.VideoDTO;
import com.suda.MyVideoApi.service.VideoService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author guhaibo
 * @date 2018/7/29
 */
@Component
@Log4j2
@AllArgsConstructor
public class DililiJob {

    private final VideoService videoService;
    private RedisTemplate redisTemplate;

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

    private void getVideos(String tag) {
        List<VideoDTO> videoDTOS;
        int page = 1;
        while ((videoDTOS = videoService.queryVideosByType(tag, page, false)).size() > 0) {
            if (videoDTOS.size() > 0) {

                //跟新页数
                String pageKey = "VideoTypeSize:" + tag;
                Integer allPage = (Integer) redisTemplate.opsForValue().get(pageKey);
                if (allPage == null) {
                    allPage = 0;
                }

                if (page > allPage) {
                    redisTemplate.opsForValue().set(pageKey, page);
                }
            }

            for (VideoDTO videoDTO : videoDTOS) {
                System.out.println(videoDTO.toString());
                VideoDetailDO videoDetailDO = videoService.queryVideosDetail(videoDTO.getVideoId(), false);


//                //缓存播放地址
//                List<VideoSeriesDO> videoSeriesDOS = videoDetailDO.getVideoSeries();
//                if (videoSeriesDOS != null) {
//                    for (VideoSeriesDO videoSeriesDO : videoSeriesDOS) {
//                        try {
//                            videoService.queryPlayUrl(videoDTO.getVideoId(), videoSeriesDO.getSeriesId());
//                        } catch (Exception e) {
//
//                        }
//                    }
//                }


            }
            page++;
        }
    }


}
