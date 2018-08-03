package com.suda.MyVideoApi.domian.dos;

import com.suda.MyVideoApi.domian.BaseDomain;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author guhaibo
 * @date 2018/7/29
 */
@Data
@NoArgsConstructor
public class VideoSeriesDO extends BaseDomain {
    private String seriesId;
    private String name;
}
