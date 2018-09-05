package com.suda.MyVideoApi.service;


import com.suda.MyVideoApi.domian.BizException;
import com.suda.MyVideoApi.domian.PageDTO;
import com.suda.MyVideoApi.domian.dos.VideoDetailDO;
import com.suda.MyVideoApi.domian.dto.VideoDTO;
import com.suda.MyVideoApi.domian.dto.VideoPlayDTO;

import java.util.List;

/**
 * @author guhaibo
 * @date 2018/7/29
 */
public interface VideoService {

    /**
     * 根据类别查询电影
     *
     * @param tag
     * @param pageIndex
     * @param useCache
     * @return
     * @throws BizException
     */
    PageDTO<VideoDTO> queryVideosByTypeNew(String tag, int pageIndex, boolean useCache) throws BizException;

    /**
     * 根据类别查询电影
     *
     * @param tag
     * @param pageIndex
     * @param useCache
     * @return
     * @throws BizException
     */
    List<VideoDTO> queryVideosByType(String tag, int pageIndex, boolean useCache) throws BizException;

    /**
     * 查询影视详情
     *
     * @param videoId
     * @param useCache
     * @return
     * @throws BizException
     */
    VideoDetailDO queryVideosDetail(String videoId, boolean useCache) throws BizException;

    /**
     * 查询播放地址
     *
     * @param videoId
     * @param seriesId
     * @return
     * @throws BizException
     */
    VideoPlayDTO queryPlayUrl(String videoId, String seriesId) throws BizException;

    /**
     * 根据关键字模糊匹配
     *
     * @param search
     * @return
     */
    List<VideoDTO> queryVideoByKey(String search);
}
