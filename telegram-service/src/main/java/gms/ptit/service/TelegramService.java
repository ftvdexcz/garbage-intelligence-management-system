package gms.ptit.service;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public interface TelegramService {
    void sendMessageHTMLAsync(String message, long chatId) throws TelegramApiException;

    void sendPhotoAsync(String message, long chatId, byte[] image) throws IOException;
}
