package com.suda.MyVideoApi.service;

import com.suda.MyVideoApi.constant.API;
import com.suda.MyVideoApi.domian.BizException;
import com.suda.MyVideoApi.domian.dos.VideoSeriesDO;
import com.suda.MyVideoApi.domian.dto.VideoDTO;
import com.suda.MyVideoApi.util.TextUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 动漫
 * @author guhaibo
 * @date 2018/7/29
 */
@Service("videoCartoonServiceImpl")
public class VideoCartoonServiceImpl extends BaseVideoService {

    @Override
    public List<VideoDTO> queryVideosByType(String tag, int pageIndex, boolean useCache) throws BizException {
        return super.queryVideosByType(tag, pageIndex, useCache);
    }


    public VideoCartoonServiceImpl(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected Elements parseItems(Document document) {
        return document.getElementsByClass("item");
    }

    @Override
    protected String parseName(Element item) {
        Elements names = item.getElementsByTag("a");
        if (names.size() > 0) {
            return names.get(0).text();
        }
        return "";
    }

    @Override
    protected String parseThumb(Element item) {
        Elements names = item.getElementsByTag("img");
        if (names.size() > 0) {
            String thumb = names.get(0).attr("src");
            if (thumb.indexOf("http") >= 0) {
                return thumb;
            } else {
                return "https:" + thumb;
            }
        }
        return "";
    }

    @Override
    public API getApi() {
        return API.CARTOON;
    }

    @Override
    protected String parseVideoDesc(Document document) {
        Elements jianjies = document.getElementsByClass("jj");
        if (jianjies.size() > 0) {
            String jianjie = jianjies.get(0).text();
            if (!TextUtil.isStrEmpty(jianjie)) {
                return jianjie.trim();
            }
        }
        return "";
    }

    @Override
    protected List<String> parsePreviewImgs(Document document) {
        List<String> previewImgs = new ArrayList<>();
        Elements carousel_inners = document.getElementsByClass("carousel-inner");
        if (carousel_inners.size() > 0) {
            Elements previewImgEl = carousel_inners.get(0).getElementsByTag("img");
            for (Element element : previewImgEl) {
                String thumb = element.attr("src");
                if (thumb.indexOf("http") >= 0) {
                    previewImgs.add(thumb);
                } else {
                    previewImgs.add("https:" + thumb);
                }
            }
        }
        return previewImgs;
    }

    @Override
    protected List<VideoSeriesDO> parseVideoSeries(Document document) {
        Element xuanji = document.getElementById("xuanji");
        if (xuanji == null) {
            return Collections.emptyList();
        }
        Elements aTagEls = xuanji.getElementsByTag("a");
        if (aTagEls.size() < 0) {
            return Collections.emptyList();
        }
        List<VideoSeriesDO> videoSeriesDOS = new ArrayList<>();
        for (Element aTagEl : aTagEls) {
            VideoSeriesDO videoSeriesDO = new VideoSeriesDO();
            videoSeriesDO.setName(aTagEl.attr("title"));
            videoSeriesDO.setSeriesId(aTagEl.attr("href")
                    .replace("?Play-", "")
                    .replace(".html", ""));
            videoSeriesDOS.add(videoSeriesDO);
        }
        return videoSeriesDOS;
    }
}
