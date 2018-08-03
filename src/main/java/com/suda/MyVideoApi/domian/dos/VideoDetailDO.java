package com.suda.MyVideoApi.domian.dos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author guhaibo
 * @date 2018/7/29
 */
@Data
@NoArgsConstructor
public class VideoDetailDO extends VideoDO {
    /**
     * 剧集
     */
    private List<VideoSeriesDO> videoSeries;
    /**
     * 描述
     */
    private String desc;
    /**
     * 预览图
     */
    private List<String> previewImgs;
}
