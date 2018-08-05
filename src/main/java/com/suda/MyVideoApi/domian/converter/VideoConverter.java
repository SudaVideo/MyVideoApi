package com.suda.MyVideoApi.domian.converter;

import com.suda.MyVideoApi.constant.API;
import com.suda.MyVideoApi.domian.DataConverter;
import com.suda.MyVideoApi.domian.dos.VideoDO;
import com.suda.MyVideoApi.domian.dto.VideoDTO;

/**
 * @author guhaibo
 * @date 2018/7/31
 */
public class VideoConverter implements DataConverter<VideoDO, VideoDTO> {

    public static final String PLAY_SPLITE = "+";

    @Override
    public void convert(VideoDO from, VideoDTO to) {
        to.setTitle(from.getTitle());
        to.setThumb(from.getThumb());
        to.setSource(from.getSource());
        API api = API.getApiBySourceId(from.getSource());
        to.setVideoId(from.getOriginUrl().replace(api.resUrl, "").replace("/", PLAY_SPLITE));
    }
}
