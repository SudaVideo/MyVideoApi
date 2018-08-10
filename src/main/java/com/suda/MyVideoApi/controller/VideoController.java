package com.suda.MyVideoApi.controller;

import com.suda.MyVideoApi.constant.API;
import com.suda.MyVideoApi.constant.Source;
import com.suda.MyVideoApi.domian.ResultDTO;
import com.suda.MyVideoApi.domian.dos.VideoDetailDO;
import com.suda.MyVideoApi.domian.dto.VideoDTO;
import com.suda.MyVideoApi.domian.dto.VideoSourceDTO;
import com.suda.MyVideoApi.service.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author guhaibo
 * @date 2018/7/29
 */
@RestController
@RequestMapping("/video")
@AllArgsConstructor
public class VideoController {

    private final VideoService videoDiliServiceImpl;
    private final VideoService videoMjwServiceImpl;
    private final VideoService videoHjServiceImpl;
    private final VideoService videoLZServiceImpl;
    private final VideoService videoCartoonServiceImpl;

    private VideoService getVideoService(int source) {
        if (source == API.DILI.sourceId) {
            return videoDiliServiceImpl;
        } else if (source == API.MJW.sourceId) {
            return videoMjwServiceImpl;
        } else if (source == API.HJ.sourceId) {
            return videoHjServiceImpl;
        } else if (source == API.LZ.sourceId) {
            return videoLZServiceImpl;
        } else if (source == API.CARTOON.sourceId) {
            return videoCartoonServiceImpl;
        }
        return null;
    }

    /**
     * 查询视频源
     *
     * @return
     */
    @GetMapping("/source")
    public ResultDTO<List<VideoSourceDTO>> qureySource() {
        //film  teleplay cartoon variety_show
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setSuccess(true);
        resultDTO.setData(Source.videoSourceDTOS);
        return resultDTO;
    }


    /**
     * 根据类别查询视频
     *
     * @param source    源id
     * @param type      类型
     * @param pageIndex 页数
     * @return
     */
    @GetMapping("/page")
    public ResultDTO<List<VideoDTO>> queryVideosByType(@RequestParam int source, @RequestParam String type, @RequestParam int pageIndex) {
        //film  teleplay cartoon variety_show
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setSuccess(true);
        resultDTO.setData(getVideoService(source).queryVideosByTypeNew(type, pageIndex, true));
        return resultDTO;
    }

    /**
     * 查询视频详情
     *
     * @param videoId 视频id
     * @param source  源id
     * @return
     */
    @GetMapping("/detail")
    public ResultDTO<VideoDetailDO> queryVideosDetail(@RequestParam String videoId, @RequestParam int source) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setSuccess(true);
        resultDTO.setData(getVideoService(source).queryVideosDetail(videoId, true));
        return resultDTO;
    }

    /**
     * 根据名称查视频
     *
     * @param videoName 视频名称
     * @return
     */
    @GetMapping("/search")
    public ResultDTO<List<VideoDTO>> queryVideoByKey(@RequestParam String videoName) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setSuccess(true);
        resultDTO.setData(videoDiliServiceImpl.queryVideoByKey(videoName));
        return resultDTO;
    }

    /**
     * 查询视频播放地址
     *
     * @param videoId  视频id
     * @param seriesId 集数id
     * @param source   源id
     * @return
     */
    @GetMapping("/playUrl")
    public ResultDTO<String> queryVideoPlayUrl(@RequestParam String videoId, @RequestParam String seriesId, @RequestParam int source) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setSuccess(true);
        resultDTO.setData(getVideoService(source).queryPlayUrl(videoId, seriesId));
        return resultDTO;
    }

}
