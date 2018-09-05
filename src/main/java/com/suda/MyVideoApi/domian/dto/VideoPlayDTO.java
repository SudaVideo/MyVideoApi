package com.suda.MyVideoApi.domian.dto;

import com.suda.MyVideoApi.domian.BaseDomain;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VideoPlayDTO extends BaseDomain {
    private String playUrl;
    private String type;

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
