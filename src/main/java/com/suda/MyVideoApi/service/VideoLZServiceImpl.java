package com.suda.MyVideoApi.service;

import com.suda.MyVideoApi.constant.API;
import com.suda.MyVideoApi.util.JsoupUtils;
import com.suda.MyVideoApi.util.SuplayerUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author guhaibo
 * @date 2018/7/29
 */
@Service("videoLZServiceImpl")
public class VideoLZServiceImpl extends BaseVideoService {

    public VideoLZServiceImpl(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public API getApi() {
        return API.LZ;
    }

    @Override
    protected String parsePlayUrl(String refererUrl) {
        Document pcDocument = JsoupUtils.getDocWithPC(refererUrl);
        Elements script = pcDocument.body().getElementsByTag("script");
        String scriptText = script.get(1).data();
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        Matcher m = pattern.matcher(scriptText);
        List<String> params = new ArrayList<>();
        while (m.find()) {
            params.add(m.group().replace("\"", ""));
        }
        String playerUrl = "http://www.lzvod.net/player/?type=" + params.get(0) + "&vkey=" + params.get(1);
        return SuplayerUtil.getPlayUrl(refererUrl, playerUrl, "http://www.lzvod.net/player/1suplayer.php");
    }
}
