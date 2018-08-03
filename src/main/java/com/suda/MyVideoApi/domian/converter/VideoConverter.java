package com.suda.MyVideoApi.domian.converter;

import com.suda.MyVideoApi.domian.DataConverter;
import com.suda.MyVideoApi.domian.dos.VideoDO;
import com.suda.MyVideoApi.domian.dto.VideoDTO;

import static com.suda.MyVideoApi.constant.URL.RES_URL;

/**
 * @author guhaibo
 * @date 2018/7/31
 */
public class VideoConverter implements DataConverter<VideoDO,VideoDTO> {

    @Override
    public void convert(VideoDO from, VideoDTO to) {
        to.setTitle(from.getTitle());
        to.setThumb(from.getThumb());
        to.setVideoId(from.getOriginUrl().replace(RES_URL, ""));
    }
}
