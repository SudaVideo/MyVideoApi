package com.suda.MyVideoApi.service;

import com.suda.MyVideoApi.constant.API;
import com.suda.MyVideoApi.domian.dos.VideoPlayDO;
import com.suda.MyVideoApi.domian.dos.VideoSeriesDO;
import com.suda.MyVideoApi.util.JsoupUtils;
import com.suda.MyVideoApi.util.SuplayerUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author guhaibo
 * @date 2018/7/29
 */
@Service("videoHjServiceImpl")
public class VideoHJServiceImpl extends BaseVideoService {

    public VideoHJServiceImpl(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public API getApi() {
        return API.HJ;
    }

    @Override
    protected VideoPlayDO parsePlayUrl(String refererUrl) {
        Document pcDocument = JsoupUtils.getDocWithPC(refererUrl);
        Elements scripts = pcDocument.body().getElementsByTag("script");
        List<String> params = new ArrayList<>();
        for (Element script : scripts) {
            if (script.toString().indexOf("var") > 0) {
                String scriptText = script.data();
                Pattern pattern = Pattern.compile("\"(.*?)\"");
                Matcher m = pattern.matcher(scriptText);
                params.clear();
                while (m.find()) {
                    params.add(m.group().replace("\"", ""));
                }
                if (params.size() >= 2) {
                    break;
                }
            }
        }
        if (params.get(1).contains("http")){
            VideoPlayDO videoPlayDO = new VideoPlayDO();
            videoPlayDO.setPlayUrl(params.get(1));
            videoPlayDO.setType("dplayer");
            return videoPlayDO;
        }

        String playerUrl = "https://api.1suplayer.me/player/?userID=&type="
                + params.get(0) + "&vkey=" + params.get(1);
        return SuplayerUtil.getPlayUrl(refererUrl, playerUrl, "https://api.1suplayer.me/player/api.php");
    }

    @Override
    protected List<VideoSeriesDO> parseVideoSeries(Document document) {
        Elements serieses = document.getElementsByClass("video_list_li");
        if (serieses == null) {
            return Collections.emptyList();
        }
        List<VideoSeriesDO> videoSeriesDOS = new ArrayList<>();
        if (serieses.size() > 0) {
            for (Element seriese : serieses) {
                for (Element element : seriese.getElementsByTag("a")) {
                    VideoSeriesDO videoSeriesDO = new VideoSeriesDO();
                    videoSeriesDO.setName(element.text());
                    videoSeriesDO.setSeriesId(element.attr("href")
                            .replace("??","?")
                            .replace("?Play-", ""));
                    videoSeriesDOS.add(videoSeriesDO);
                }
            }
        }
        return videoSeriesDOS;
    }
}
