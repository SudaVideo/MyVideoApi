package com.suda.MyVideoApi.domian.dto;

import com.suda.MyVideoApi.domian.BaseDomain;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author guhaibo
 * @date 2018/8/11
 */

@Data
@NoArgsConstructor
public class SiteInfoDTO extends BaseDomain {
    private String siteName;
    private String contactEmail;
}
