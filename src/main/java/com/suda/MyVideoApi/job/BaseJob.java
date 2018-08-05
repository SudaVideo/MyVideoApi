package com.suda.MyVideoApi.job;

import com.suda.MyVideoApi.domian.dos.VideoDetailDO;
import com.suda.MyVideoApi.domian.dto.VideoDTO;
import com.suda.MyVideoApi.service.BaseVideoService;
import com.suda.MyVideoApi.service.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @author guhaibo
 * @date 2018/8/5
 */
@AllArgsConstructor
public class BaseJob<T extends BaseVideoService> {

    private T videoService;
    private RedisTemplate redisTemplate;

    protected void getVideos(String tag) {
        List<VideoDTO> videoDTOS;
        int page = 1;
        while ((videoDTOS = videoService.queryVideosByType(tag, page, false)).size() > 0) {
            if (videoDTOS.size() > 0) {
                //跟新页数
                String pageKey = videoService.getApi().name() + ":" + "VideoTypeSize:" + tag;
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
                try {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            page++;
        }
    }

}
