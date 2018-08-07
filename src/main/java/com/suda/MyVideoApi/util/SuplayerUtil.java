package com.suda.MyVideoApi.util;

import com.alibaba.fastjson.JSONObject;
import com.suda.MyVideoApi.constant.API;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author guhaibo
 * @date 2018/8/5
 */
public class SuplayerUtil {

    public static String getPlayUrl(String refereUrl, int source) {
        Document pcDocument = JsoupUtils.getDocWithPC(refereUrl);
        if (source == API.CARTOON.sourceId) {
            String playerUrl  = pcDocument.getElementsByClass("player").first().attr("src");
            return getPlayUrl(refereUrl, playerUrl, "https://api.1suplayer.me/player/api.php");
        } else {
            Elements script = pcDocument.body().getElementsByTag("script");
            String scriptText = "";
            if (source == API.DILI.sourceId || source == API.LZ.sourceId) {
                scriptText = script.get(1).data();
            } else {
                scriptText = script.get(0).data();
            }
            Pattern pattern = Pattern.compile("\"(.*?)\"");
            Matcher m = pattern.matcher(scriptText);
            List<String> params = new ArrayList<>();
            while (m.find()) {
                params.add(m.group().replace("\"", ""));
            }
            if (source == API.LZ.sourceId) {
                String playerUrl = "http://www.lzvod.net/player/?type=" + params.get(0) + "&vkey=" + params.get(1);
                return getPlayUrl(refereUrl, playerUrl, "http://www.lzvod.net/player/1suplayer.php");
            } else {
                String playerUrl = "https://api.1suplayer.me/player/?userID=&type="
                        + params.get(0) + "&vkey=" + params.get(1);
                return getPlayUrl(refereUrl, playerUrl, "https://api.1suplayer.me/player/api.php");
            }
        }
    }

    private static String getPlayUrl(String refererUrl, String playerUrl, String api) {
        try {
            Document pcDocument = JsoupUtils
                    .getConnection(playerUrl)
                    .header("Referer", refererUrl)
                    .get();

            Elements script = pcDocument.body().getElementsByTag("script");
            String scriptText = script.get(0).data();
            Pattern pattern = Pattern.compile(" = '(.+?)'");
            Matcher m = pattern.matcher(scriptText);

            List<String> params = new ArrayList<>();
            while (m.find()) {
                params.add(m.group().replace("= '", "").replace("'", "").trim());
            }

            Map<String, String> map = new HashMap<>();
            map.put("type", params.get(3));
            map.put("vkey", params.get(4));
            map.put("ckey", params.get(2));
            map.put("userID", "");
            map.put("userIP", params.get(0));
            map.put("refres", "1");
            map.put("my_url", params.get(1));

            int tryTime = 5;
            String playUrl = null;
            while (tryTime > 0 && TextUtil.isStrEmpty(playUrl)) {
                Document document = JsoupUtils
                        .getConnection(api)
                        .header("Referer", playerUrl)
                        .data(map)
                        .post();
                try {
                    playUrl = JSONObject.parseObject(document.body().text()).getString("url");
                } catch (Exception e) {
                }
                tryTime--;
            }
            return playUrl;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
