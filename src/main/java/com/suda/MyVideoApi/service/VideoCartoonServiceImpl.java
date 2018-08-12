package com.suda.MyVideoApi.service;

import com.suda.MyVideoApi.constant.API;
import com.suda.MyVideoApi.domian.BizException;
import com.suda.MyVideoApi.domian.dos.VideoSeriesDO;
import com.suda.MyVideoApi.domian.dto.VideoDTO;
import com.suda.MyVideoApi.util.JsoupUtils;
import com.suda.MyVideoApi.util.SuplayerUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 动漫
 *
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
        Element nameEl = item.getElementsByTag("a").first();
        if (nameEl != null) {
            return nameEl.text();
        }
        return "";
    }

    @Override
    protected String parseThumb(Element item) {
        Element thumbEl = item.getElementsByTag("img").first();
        if (thumbEl != null) {
            String thumb = thumbEl.attr("src");
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
        Element descEl = document.getElementsByClass("jj").first();
        if (descEl != null) {
            String desc = descEl.text();
            if (!StringUtils.isEmpty(desc)) {
                return desc.trim();
            }
        }
        return "";
    }

    @Override
    protected List<String> parsePreviewImgs(Document document) {
        List<String> previewImgs = new ArrayList<>();
        Element carouselInnersEl = document.getElementsByClass("carousel-inner").first();
        if (carouselInnersEl != null) {
            Elements previewImgEl = carouselInnersEl.getElementsByTag("img");
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

    @Override
    protected String parsePlayUrl(String refererUrl) {
        Document pcDocument = JsoupUtils.getDocWithPC(refererUrl);
        String playerUrl = pcDocument.getElementsByClass("player").first().attr("src");
        return SuplayerUtil.getPlayUrl(refererUrl, playerUrl, "https://api.1suplayer.me/player/api.php");
    }
}
