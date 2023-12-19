package gms.ptit.service.impl;

import gms.ptit.service.TelegramService;
import gms.ptit.service.bot.BotPolling;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Slf4j
@Service
public class TelegramServiceImpl implements TelegramService {
    @Autowired
    BotPolling botPolling;
    
    @Override
    public void sendMessageHTMLAsync(String message, long chatId) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(message);
        sendMessage.setChatId(chatId);
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.disableWebPagePreview();
        botPolling.executeAsync(sendMessage, new SentCallback<Message>() {
            @Override
            public void onResult(BotApiMethod<Message> botApiMethod, Message message) {
                log.info("[TelegramServiceImpl - onResult] send message success");
            }

            @Override
            public void onError(BotApiMethod<Message> botApiMethod, TelegramApiRequestException e) {
                log.info("[TelegramServiceImpl - onResult] send message error: {}", e.getMessage());
            }

            @Override
            public void onException(BotApiMethod<Message> botApiMethod, Exception e) {
                log.info("[TelegramServiceImpl - onResult] send message exception: {}", e.getMessage());
            }
        });
    }

    @Override
    public void sendPhotoAsync(String message, long chatId, byte[] image) throws IOException {
        InputFile inputFile = new InputFile();
        inputFile.setMedia(new ByteArrayInputStream(image), "image");
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setParseMode(ParseMode.HTML);
        sendPhoto.setCaption(message);
        sendPhoto.setPhoto(inputFile);
        botPolling.executeAsync(sendPhoto);
    }
}
