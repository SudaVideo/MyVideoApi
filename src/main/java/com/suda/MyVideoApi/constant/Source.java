package com.suda.MyVideoApi.constant;

import com.suda.MyVideoApi.domian.dto.VideoSourceDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guhaibo
 * @date 2018/8/5
 */
public class Source {
    public static List<VideoSourceDTO> videoSourceDTOS = new ArrayList<>();

    static {
        videoSourceDTOS.add(new VideoSourceDTO(API.DILI.sourceId, "film", "电影1"));
        videoSourceDTOS.add(new VideoSourceDTO(API.LZ.sourceId, "movies", "电影2"));

        videoSourceDTOS.add(new VideoSourceDTO(API.DILI.sourceId, "teleplay", "剧集1"));
        videoSourceDTOS.add(new VideoSourceDTO(API.LZ.sourceId, "teleplay", "剧集2"));

        videoSourceDTOS.add(new VideoSourceDTO(API.DILI.sourceId, "cartoon", "动漫1"));
        videoSourceDTOS.add(new VideoSourceDTO(API.LZ.sourceId, "cartoon", "动漫2"));

        videoSourceDTOS.add(new VideoSourceDTO(API.DILI.sourceId, "variety_show", "综艺1"));
        videoSourceDTOS.add(new VideoSourceDTO(API.LZ.sourceId, "variety", "综艺2"));

        videoSourceDTOS.add(new VideoSourceDTO(API.DILI.sourceId, "list11", "记录1"));
        videoSourceDTOS.add(new VideoSourceDTO(API.LZ.sourceId, "fact", "记录2"));

        videoSourceDTOS.add(new VideoSourceDTO(API.MJW.sourceId, "all_mj", "美剧"));
        videoSourceDTOS.add(new VideoSourceDTO(API.HJ.sourceId, "hanju", "韩剧"));
    }
}
