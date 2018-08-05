package com.suda.MyVideoApi.domian;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author guhaibo
 * @date 2018/8/3
 */
@Data
@NoArgsConstructor
public class PageDTO<T> {
    private int pageSize;
    private List<T> data;
}
