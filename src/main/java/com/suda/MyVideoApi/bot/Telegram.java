package com.suda.MyVideoApi.bot;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.suda.MyVideoApi.domian.dto.VideoDTO;
import com.suda.MyVideoApi.service.VideoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author guhaibo
 * @date 2018/9/12
 */
@Component
public class Telegram {


    private TelegramBot telegramBot;

    private VideoService videoDiliServiceImpl;

    public Telegram(VideoService videoDiliServiceImpl, @Value("${spring.botToken}") String botToken) {
        this.videoDiliServiceImpl = videoDiliServiceImpl;
        if (StringUtils.isEmpty(botToken)){
            return;
        }
        telegramBot = new TelegramBot(botToken);
        telegramBot.setUpdatesListener(new UpdatesListener() {
            @Override
            public int process(List<Update> updates) {
                for (Update update : updates) {
                    String chatId = update.message().chat().id().toString();
                    String messageText = update.message().text();
                    List<VideoDTO> videoDTOS = videoDiliServiceImpl.queryVideoByKey(messageText);
                    if (videoDTOS.size() > 0) {
                        for (VideoDTO videoDTO : videoDTOS) {
                            sendTextMessage(chatId, update.message().messageId(),
                                    videoDTO.getTitle() + ":https://sudavideo.site/#/videoDetail/" + videoDTO.getSource() + "/" + videoDTO.getVideoId());
                        }
                    } else {
                        sendTextMessage(chatId, update.message().messageId(), "未查询到");
                    }

                }
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        });
    }

    public void sendTextMessage(String chatId, int replayId, String text) {
        SendMessage request = new SendMessage(chatId, text)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(false)
                .disableNotification(false)
                .replyToMessageId(replayId);
        telegramBot.execute(request, new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage request, SendResponse response) {
//                System.out.println(response);
            }

            @Override
            public void onFailure(SendMessage request, IOException e) {
//                e.printStackTrace();
            }
        });
    }
}
