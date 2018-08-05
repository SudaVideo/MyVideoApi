package com.suda.MyVideoApi.domian.dto;

import com.suda.MyVideoApi.domian.BaseDomain;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author guhaibo
 * @date 2018/7/29
 */

@Data
@NoArgsConstructor
public class VideoDTO extends BaseDomain {
    private String title;
    private String thumb;
    private String videoId;
    private int source;
}
