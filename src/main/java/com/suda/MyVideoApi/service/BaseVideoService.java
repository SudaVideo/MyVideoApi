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
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collections;
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

    protected RedisTemplate redisTemplate;

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

            for (Element article : parseItems(pcDocument)) {
                VideoDO videoDO = new VideoDO();
                videoDO.setSource(getApi().sourceId);
                videoDO.setTitle(parseName(article));
                videoDO.setOriginUrl(parseOriginUrl(article));
                videoDO.setThumb(parseThumb(article));
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

            Document pcDocument = JsoupUtils.getDocWithPC(getApi().resUrl + videoId.replace(PLAY_SPLITE, "/"));
            videoDetailDO.setDesc(parseVideoDesc(pcDocument));
            videoDetailDO.setPreviewImgs(parsePreviewImgs(pcDocument));
            videoDetailDO.setVideoSeries(parseVideoSeries(pcDocument));

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
        int max = Math.min(5, keys.size());
        int i = 0;
        for (String key : keys) {
            if (i == max) {
                break;
            }
            VideoDO videoDO = (VideoDO) redisTemplate.opsForValue().get(key);
            try {
                VideoDTO videoDTO = new VideoDTO();
                VideoConverter videoConverter = new VideoConverter();
                videoConverter.convert(videoDO, videoDTO);
                videoDTOS.add(videoDTO);
                i++;
            } catch (Exception e) {
                continue;
            }
        }
        return videoDTOS;
    }

    /**
     * 解析条目
     *
     * @param document
     * @return
     */
    protected Elements parseItems(Document document) {
        return document.getElementsByTag("article");
    }

    /**
     * 解析标题
     *
     * @param item
     * @return
     */
    protected String parseName(Element item) {
        Elements names = item.getElementsByTag("h2");
        if (names.size() > 0) {
            return names.get(0).text();
        }
        return "";
    }

    /**
     * 解析封面
     *
     * @param item
     * @return
     */
    protected String parseThumb(Element item) {
        Elements thumbs = item.getElementsByClass("thumb");
        if (thumbs.size() > 0) {
            String thumb = thumbs.get(0).attr("data-original");
            if (thumb.indexOf("http") >= 0) {
                return thumb;
            } else {
                return "https:" + thumb;
            }
        }
        return "";
    }

    /**
     * 解析原始地址
     *
     * @param item
     * @return
     */
    protected String parseOriginUrl(Element item) {
        Elements urls = item.getElementsByTag("a");
        if (urls.size() > 0) {
            return urls.get(0).attr("href");
        }
        return "";
    }

    /**
     * 解析介绍
     *
     * @param document
     * @return
     */
    protected String parseVideoDesc(Document document) {
        Elements jianjies = document.getElementsByClass("jianjie");
        if (jianjies.size() > 0) {
            jianjies = jianjies.get(0).getElementsByTag("span");
            if (jianjies.size() > 0) {
                String jianjie = jianjies.get(0).text();
                if (!TextUtil.isStrEmpty(jianjie)) {
                    return jianjie.trim();
                }
            }
        }
        return "";
    }

    /**
     * 解析预览图
     *
     * @param document
     * @return
     */
    protected List<String> parsePreviewImgs(Document document) {
        List<String> previewImgs = new ArrayList<>();
        Elements jianjies = document.getElementsByClass("jianjie");
        if (jianjies.size() > 0) {
            Elements previewImgEl = jianjies.get(0).getElementsByTag("img");
            for (Element element : previewImgEl) {
                String thumb = element.attr("data-original");
                if (thumb.indexOf("http") >= 0) {
                    previewImgs.add(thumb);
                } else {
                    previewImgs.add("https:" + thumb);
                }
            }
        }
        return previewImgs;
    }
    /**
     * 解析剧集
     *
     * @param document
     * @return
     */
    protected List<VideoSeriesDO> parseVideoSeries(Document document) {
        List<Elements> elementsList = new ArrayList<>();
        elementsList.add(document.getElementsByClass("mplay-list"));
        elementsList.add(document.getElementsByClass("article-paging"));
        elementsList.add(document.getElementsByClass("video_list_li"));
        Elements serieses = null;
        for (Elements elements : elementsList) {
            if (elements.size() > 0) {
                serieses = elements;
                break;
            }
        }
        if (serieses == null) {
            return Collections.emptyList();
        }

        List<VideoSeriesDO> videoSeriesDOS = new ArrayList<>();
        if (serieses.size() > 0) {
            for (Element seriese : serieses) {
                for (Element element : seriese.getElementsByTag("a")) {
                    VideoSeriesDO videoSeriesDO = new VideoSeriesDO();
                    videoSeriesDO.setName(element.text());
                    videoSeriesDO.setSeriesId(element.attr("href").replace("?Play=", ""));
                    videoSeriesDOS.add(videoSeriesDO);
                }
            }
        }
        return videoSeriesDOS;
    }
}
