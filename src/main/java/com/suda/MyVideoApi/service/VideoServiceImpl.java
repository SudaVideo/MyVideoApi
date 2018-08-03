package com.suda.MyVideoApi.service;

import com.alibaba.fastjson.JSONObject;
import com.suda.MyVideoApi.domian.BizException;
import com.suda.MyVideoApi.domian.PageDTO;
import com.suda.MyVideoApi.domian.converter.VideoConverter;
import com.suda.MyVideoApi.domian.dos.VideoDO;
import com.suda.MyVideoApi.domian.dos.VideoDetailDO;
import com.suda.MyVideoApi.domian.dos.VideoSeriesDO;
import com.suda.MyVideoApi.domian.dto.VideoDTO;
import com.suda.MyVideoApi.util.JsoupUtils;
import com.suda.MyVideoApi.util.TextUtil;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.suda.MyVideoApi.constant.URL.BASE_URL;
import static com.suda.MyVideoApi.constant.URL.RES_SERIES_URL;
import static com.suda.MyVideoApi.constant.URL.RES_URL;

/**
 * @author guhaibo
 * @date 2018/7/29
 */
@Service
@AllArgsConstructor
public class VideoServiceImpl implements VideoService {

    private RedisTemplate redisTemplate;

    @Override
    public PageDTO queryVideosByTypeNew(String tag, int pageIndex, boolean useCache) throws BizException {
        PageDTO pageDTO = new PageDTO();
        String pageKey = "VideoTypeSize:" + tag;
        Integer allPage = (Integer) redisTemplate.opsForValue().get(pageKey);
        if (allPage == null) {
            allPage = 0;
        }
        pageDTO.setPageSize(allPage);
        pageDTO.setData(queryVideosByType(tag, pageIndex, useCache));
        return pageDTO;
    }

    @Override
    public List<VideoDTO> queryVideosByType(String tag, int pageIndex, boolean useCache) throws BizException {
        String key = "VideoType:" + tag + ":" + pageIndex;
        List<VideoDO> videoDOS = (List<VideoDO>) redisTemplate.opsForValue().get(key);
        if (!useCache) {
            videoDOS = null;
        }
        String pageKey = "VideoTypeSize:" + tag;
        Integer allPage = (Integer) redisTemplate.opsForValue().get(pageKey);
        if (allPage == null) {
            allPage = 0;
        }
        if (videoDOS == null) {
            videoDOS = new ArrayList<>();
            String video = BASE_URL + tag;
            if (pageIndex > 1) {
                video = video + "/page/" + pageIndex;
            }
            Document pcDocument = JsoupUtils.getDocWithPC(video);
            Elements articles = pcDocument.getElementsByTag("article");

            for (Element article : articles) {
                VideoDO videoDO = new VideoDO();
                Elements names = article.getElementsByTag("h2");
                if (names.size() > 0) {
                    videoDO.setTitle(names.get(0).text());
                }
                Elements thumbs = article.getElementsByClass("thumb");
                if (thumbs.size() > 0) {
                    String Thumb = thumbs.get(0).attr("data-original");
                    if (Thumb.indexOf("http") >= 0) {
                        videoDO.setThumb(Thumb);
                    } else {
                        videoDO.setThumb("https:" + Thumb);
                    }
                }

                Elements urls = article.getElementsByTag("a");
                if (urls.size() > 0) {
                    videoDO.setOriginUrl(urls.get(0).attr("href"));
                }
                videoDOS.add(videoDO);

                String key2 = "VideoTitle:" + videoDO.getTitle();
                redisTemplate.opsForValue().set(key2, videoDO);

            }
            redisTemplate.opsForValue().set(key, videoDOS);
        }
        List<VideoDTO> videoDTOS = new ArrayList<>();
        for (VideoDO videoDO : videoDOS) {
            try {
                VideoConverter videoConverter = new VideoConverter();
                VideoDTO videoDTO = new VideoDTO();
                videoDTO.setPageSize(allPage);
                videoConverter.convert(videoDO, videoDTO);
                videoDTOS.add(videoDTO);
            } catch (Exception e) {
                continue;
            }
        }

        return videoDTOS;
    }

    @Override
    public VideoDetailDO queryVideosDetail(String videoId, boolean useCache) {
        String key = "VideoDetailDO:" + videoId;
        VideoDetailDO videoDetailDO = (VideoDetailDO) redisTemplate.opsForValue().get(key);

        if (!useCache) {
            videoDetailDO = null;
        }

        if (videoDetailDO == null) {
            videoDetailDO = new VideoDetailDO();
            getVideoDetail(RES_URL + videoId, videoDetailDO);
            redisTemplate.opsForValue().set(key, videoDetailDO);
        }
        return videoDetailDO;
    }

    @Override
    public String queryPlayUrl(String videoId, String seriesId) throws BizException {
        String key = "VideoPlayUrl:" + videoId + ":" + seriesId;
        String playUrl = (String) redisTemplate.opsForValue().get(key);

        playUrl = null;
        if (playUrl == null) {
            String seriesUrl = String.format(RES_SERIES_URL, videoId, seriesId);
            playUrl = getPlayUrl(seriesUrl);
            redisTemplate.opsForValue().set(key, playUrl);
        }
        return playUrl;
    }

    @Override
    public List<VideoDTO> queryVideoByKey(String search) {
        String pattern = "VideoTitle:*" + search + "*";
        Set<String> keys = redisTemplate.keys(pattern);
        List<VideoDTO> videoDTOS = new ArrayList<>();
        for (String key : keys) {
            VideoDO videoDO = (VideoDO) redisTemplate.opsForValue().get(key);
            try {
                VideoDTO videoDTO = new VideoDTO();
                VideoConverter videoConverter = new VideoConverter();
                videoConverter.convert(videoDO, videoDTO);
                videoDTOS.add(videoDTO);
            } catch (Exception e) {
                continue;
            }
        }
        return videoDTOS;
    }

    private void getVideoDetail(String orgUrl, VideoDetailDO videoDetailDO) {
        Document pcDocument = JsoupUtils.getDocWithPC(orgUrl);
        Elements jianjies = pcDocument.getElementsByClass("jianjie");
        if (jianjies.size() > 0) {
            List<String> previewImgs = new ArrayList<>();

            videoDetailDO.setPreviewImgs(previewImgs);

            Elements previewImgEl = jianjies.get(0).getElementsByTag("img");
            for (Element element : previewImgEl) {
                String Thumb = element.attr("data-original");
                if (Thumb.indexOf("http") >= 0) {
                    previewImgs.add(Thumb);
                } else {
                    previewImgs.add("https:" + Thumb);
                }
            }
            jianjies = jianjies.get(0).getElementsByTag("span");
            if (jianjies.size() > 0) {
                String jianjie = jianjies.get(0).text();
                if (!TextUtil.isStrEmpty(jianjie)) {
                    videoDetailDO.setDesc(jianjie.trim());
                }
            }
        }


        Elements mplay_list = pcDocument.getElementsByClass("mplay-list");
        Elements article_paging = pcDocument.getElementsByClass("article-paging");

        Elements serieses = mplay_list.size() > 0 ? mplay_list : article_paging;
        List<VideoSeriesDO> videoSeriesDOS = new ArrayList<>();
        videoDetailDO.setVideoSeries(videoSeriesDOS);
        if (serieses.size() > 0) {
            for (Element element : serieses.get(0).getElementsByTag("a")) {
                VideoSeriesDO videoSeriesDO = new VideoSeriesDO();
                videoSeriesDO.setName(element.text());

                videoSeriesDO.setName(element.text());
                videoSeriesDO.setSeriesId(element.attr("href").replace("?Play=", ""));
                String resUrl = orgUrl + element.attr("href");
//                try {
//                    videoSeriesDO.setPlayUrl(getPlayUrl(resUrl));
//                }catch (Exception e){
//                    continue;
//                }
//                if (videoSeriesDO.getPlayUrl() != null) {
//                    videoSeriesDOS.add(videoSeriesDO);
//                }

                videoSeriesDOS.add(videoSeriesDO);
            }
        }
    }

    private String getPlayUrl(String playerUrl) {
        Document pcDocument = JsoupUtils.getDocWithPC(playerUrl);
        Elements script = pcDocument.body().getElementsByTag("script");
        String scriptText = script.get(1).data();
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        Matcher m = pattern.matcher(scriptText);
        List<String> params = new ArrayList<>();
        while (m.find()) {
            params.add(m.group().replace("\"", ""));
        }
        String playerUrl2 = "https://api.1suplayer.me/player/?userID=&type="
                + params.get(0) + "&vkey=" + params.get(1);
        return getPlayUrl(playerUrl, playerUrl2);
    }

    private String getPlayUrl(String playerUrl, String playerUrl2) {
        try {
            Document pcDocument = JsoupUtils
                    .getConnection(playerUrl2)
                    .header("Host", "api.1suplayer.me")
                    .header("Referer", playerUrl)
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
                        .getConnection("https://api.1suplayer.me/player/api.php")
                        .header("Host", "api.1suplayer.me")
                        .header("Referer", playerUrl2)
                        .header("Origin", "https://api.1suplayer.me")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
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
