package com.suda.MyVideoApi.domian.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author guhaibo
 * @date 2018/8/5
 */
@Data
@AllArgsConstructor
public class VideoSourceDTO {
    private int source;
    private String type;
    private String name;
}
