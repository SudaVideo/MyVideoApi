package com.suda.MyVideoApi.controller;

import com.suda.MyVideoApi.domian.PageDTO;
import com.suda.MyVideoApi.domian.ResultDTO;
import com.suda.MyVideoApi.domian.dos.VideoDetailDO;
import com.suda.MyVideoApi.domian.dto.VideoDTO;
import com.suda.MyVideoApi.service.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author guhaibo
 * @date 2018/7/29
 */
@RestController
@RequestMapping("/video")
@AllArgsConstructor
public class VideoController {


    private final VideoService videoService;

    /**
     * 根据类别查询视频
     *
     * @param tag
     * @param pageIndex
     * @return
     */
    @GetMapping("/{tag}/{pageIndex}")
    public ResultDTO<List<VideoDTO>> queryVideosByType(@PathVariable String tag, @PathVariable int pageIndex) {
        //film  teleplay cartoon variety_show
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setSuccess(true);
        resultDTO.setData(videoService.queryVideosByType(tag, pageIndex, true));
        return resultDTO;
    }

    /**
     * 根据类别查询视频
     *
     * @param tag
     * @param pageIndex
     * @return
     */
    @GetMapping("/page/{tag}/{pageIndex}")
    public ResultDTO<PageDTO> queryVideosByTypeNew(@PathVariable String tag, @PathVariable int pageIndex) {
        //film  teleplay cartoon variety_show
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setSuccess(true);
        resultDTO.setData(videoService.queryVideosByTypeNew(tag, pageIndex, true));
        return resultDTO;
    }

    /**
     * 查询视频详情
     *
     * @param videoId
     * @return
     */
    @GetMapping("/detail/{videoId}")
    public ResultDTO<VideoDetailDO> queryVideosDetail(@PathVariable String videoId) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setSuccess(true);
        resultDTO.setData(videoService.queryVideosDetail(videoId, true));
        return resultDTO;
    }

    /**
     * 查询视频播放地址
     *
     * @param key
     * @return
     */
    @GetMapping("/search/{key}")
    public ResultDTO<List<VideoDTO>> queryVideoByKey(@PathVariable String key) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setSuccess(true);
        resultDTO.setData(videoService.queryVideoByKey(key));
        return resultDTO;
    }

    /**
     * 查询视频播放地址
     *
     * @param videoId
     * @return
     */
    @GetMapping("/playUrl/{videoId}/{seriesId}")
    public ResultDTO<String> queryVideoPlayUrl(@PathVariable String videoId, @PathVariable String seriesId) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setSuccess(true);
        resultDTO.setData(videoService.queryPlayUrl(videoId, seriesId));
        return resultDTO;
    }

}
