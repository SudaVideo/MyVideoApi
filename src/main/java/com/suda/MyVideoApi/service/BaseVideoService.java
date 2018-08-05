package com.suda.MyVideoApi.service;

import com.suda.MyVideoApi.constant.API;
import com.suda.MyVideoApi.domian.BizException;
import com.suda.MyVideoApi.domian.PageDTO;
import com.suda.MyVideoApi.domian.converter.VideoConverter;
import com.suda.MyVideoApi.domian.dos.VideoDO;
import com.suda.MyVideoApi.domian.dos.VideoDetailDO;
import com.suda.MyVideoApi.domian.dos.VideoSeriesDO;
import com.suda.MyVideoApi.domian.dto.VideoDTO;
import com.suda.MyVideoApi.util.JsoupUtils;
import com.suda.MyVideoApi.util.SuplayerUtil;
import com.suda.MyVideoApi.util.TextUtil;
import com.sun.xml.internal.bind.v2.model.core.ID;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.suda.MyVideoApi.domian.converter.VideoConverter.PLAY_SPLITE;

/**
 * @author guhaibo
 * @date 2018/7/29
 */
@AllArgsConstructor
public abstract class BaseVideoService implements VideoService {

    public abstract API getApi();

    private RedisTemplate redisTemplate;

    @Override
    public PageDTO<VideoDTO> queryVideosByTypeNew(String tag, int pageIndex, boolean useCache) throws BizException {
        PageDTO pageDTO = new PageDTO();
        String pageKey = getApi().name() + ":" + "VideoTypeSize:" + tag;
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
        String key = getApi().name() + ":" + "VideoType:" + tag + ":" + pageIndex;
        List<VideoDO> videoDOS = (List<VideoDO>) redisTemplate.opsForValue().get(key);
        if (!useCache) {
            videoDOS = null;
        }

        if (videoDOS == null) {
            videoDOS = new ArrayList<>();
            String video = getApi().baseUrl + tag;
            if (pageIndex > 1) {
                video = video + "/page/" + pageIndex;
            }
            Document pcDocument = JsoupUtils.getDocWithPC(video);
            Elements articles = pcDocument.getElementsByTag("article");

            for (Element article : articles) {
                VideoDO videoDO = new VideoDO();
                videoDO.setSource(getApi().sourceId);
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

//                String key3 = "VideoId:" + videoDO.getOriginUrl().replace(getBaseResUrl(), "");
//                redisTemplate.opsForValue().set(key3, videoDO);

            }
            redisTemplate.opsForValue().set(key, videoDOS);
        }

        List<VideoDTO> videoDTOS = new ArrayList<>();
        for (VideoDO videoDO : videoDOS) {
            try {
                VideoConverter videoConverter = new VideoConverter();
                VideoDTO videoDTO = new VideoDTO();
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
        String key = getApi().name() + ":" + "VideoDetailDO:" + videoId;
        VideoDetailDO videoDetailDO = (VideoDetailDO) redisTemplate.opsForValue().get(key);


        if (!useCache) {
            videoDetailDO = null;
        }

        if (videoDetailDO == null) {
            videoDetailDO = new VideoDetailDO();
            getVideoDetail(getApi().resUrl + videoId, videoDetailDO);
            redisTemplate.opsForValue().set(key, videoDetailDO);
        }
        return videoDetailDO;
    }

    @Override
    public String queryPlayUrl(String videoId, String seriesId) throws BizException {
        String key = getApi().name() + ":" + "VideoPlayUrl:" + videoId + ":" + seriesId;
        String playUrl = (String) redisTemplate.opsForValue().get(key);
        if (playUrl == null) {
            String seriesUrl = String.format(getApi().resSeriresUrl, videoId, seriesId).replace(PLAY_SPLITE, "/");
            playUrl = SuplayerUtil.getPlayUrl(seriesUrl, getApi().sourceId);
            redisTemplate.opsForValue().set(key, playUrl);
            redisTemplate.expire(key, 1, TimeUnit.HOURS);
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
        Document pcDocument = JsoupUtils.getDocWithPC(orgUrl.replace(PLAY_SPLITE, "/"));
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


        List<Elements> elementsList = new ArrayList<>();
        elementsList.add(pcDocument.getElementsByClass("mplay-list"));
        elementsList.add(pcDocument.getElementsByClass("article-paging"));
        elementsList.add(pcDocument.getElementsByClass("video_list_li"));


        Elements serieses = null;
        for (Elements elements : elementsList) {
            if (elements.size() > 0) {
                serieses = elements;
                break;
            }
        }

        if (serieses == null) {
            return;
        }


        List<VideoSeriesDO> videoSeriesDOS = new ArrayList<>();
        videoDetailDO.setVideoSeries(videoSeriesDOS);
        if (serieses.size() > 0) {

            for (Element seriese : serieses) {
                for (Element element : seriese.getElementsByTag("a")) {
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
    }
}
