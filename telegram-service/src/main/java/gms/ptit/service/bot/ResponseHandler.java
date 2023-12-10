package gms.ptit.service.bot;

import gms.ptit.constant.Constants;
import gms.ptit.constant.EventType;
import gms.ptit.constant.State;
import gms.ptit.mysql.entity.UserChatInfoEntity;
import gms.ptit.service.UserChatInfoService;
import gms.ptit.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ResponseHandler {
    private SilentSender sender;
    private Map<Long, State> registerStates;

    @Autowired
    UserChatInfoService userChatInfoService;

    public void setSenderAndDBState(SilentSender sender, DBContext db) {
        this.sender = sender;
        registerStates = db.getMap(Constants.CHAT_STATES);
    }

    public void replyHandler(BaseAbilityBot baseAbilityBot, Update update) {
        String message = update.getMessage().getText();
        long chatId = AbilityUtils.getChatId(update);

        if (message.equalsIgnoreCase("/cancel")) {
            cancelHandler(baseAbilityBot, update);
        }
        switch (registerStates.get(chatId)) {
            case PICK_EVENT_TYPE -> pikEventTypeHandler(baseAbilityBot, update);
            default -> unexpectedMessage(baseAbilityBot, update);
        }
    }

    public void leftGroupHandler(long chatId){
        log.info("[ResponseHandler - leftGroupHandler] chatId: {}", chatId);
        userChatInfoService.deleteWithChatId(chatId);
    }

    public void joinGroupHandler(long chatId){
        log.info("[ResponseHandler - joinGroupHandler] chatId: {}", chatId);
        userChatInfoService.subscribeWithChatId(chatId, EventType.EVENT_TYPE_ALL.code);
    }

    public void unsubscribeHandler(MessageContext ctx){
        long chatId = ctx.chatId();
        log.info("[ResponseHandler - unsubscribeHandler] chatId: {}", chatId);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        int deleted = userChatInfoService.deleteWithChatId(chatId);
        if(deleted == 0){
            message.setText("Bạn chưa đăng ký dịch vụ, để đăng ký dịch vụ nhập /subscribe");
        }else{
            message.setText("Hủy đăng ký thành công. Để đăng ký lại, nhập /subscribe");
        }
        sender.execute(message);
        cancelHandler(ctx.bot(), ctx.update());
    }

    public void subscribeHandler(MessageContext ctx) {
        long chatId = ctx.chatId();
        UserChatInfoEntity userChatInfo = userChatInfoService.getUserInfoByChatId(String.valueOf(chatId));
        log.info("[ResponseHandler - subscribeHandler] userChatInfo: {}", userChatInfo);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        if(userChatInfo != null){
            message.setText(String.format("Bạn đã đăng ký dịch vụ vào lúc %s. Chọn lại sự kiện muốn nhận hoặc ấn /cancel để kết thúc", DateUtils.formattedDate(userChatInfo.getCreatedDate())));
        }else{
            message.setText("Vui lòng chọn loại sự kiện muốn nhận thông báo...");
        }
        KeyboardRow row = new KeyboardRow();
        row.add(EventType.EVENT_TYPE_LOAD_CELL.message);
        row.add(EventType.EVENT_TYPE_INVALID_PLATE_DETECT.message);
        row.add(EventType.EVENT_TYPE_ALL.message);
        message.setReplyMarkup(new ReplyKeyboardMarkup(List.of(row)));
        sender.execute(message);
        registerStates.put(chatId, State.PICK_EVENT_TYPE);
    }

    public void pikEventTypeHandler(BaseAbilityBot baseAbilityBot, Update update){
        long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();
        log.info("[ResponseHandler - pikEventTypeHandler] picked: {}, chatId: {}", message, chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        for (EventType eventType : EventType.values()) {
            if (eventType.message.equals(message)) {
                int status = userChatInfoService.subscribeWithChatId(chatId, eventType.code);
                if(status == Constants.UPDATE_EVENT_TYPE_SUBSCRIBE_STATUS){
                    sendMessage.setText("Cập nhật sự kiện thành công: " + eventType.message);
                }else if(status == Constants.CREATE_EVENT_TYPE_SUBSCRIBE_STATUS){
                    sendMessage.setText("Đăng ký dịch vụ thành công, bạn sẽ nhận được thông báo khi có sự kiện: " + eventType.message);
                }
                sender.execute(sendMessage);
                cancelHandler(baseAbilityBot, update);
                return;
            }
        }
        sendMessage.setText("Sự kiện đã chọn không hợp lệ vui lòng chọn lại");
        sender.execute(sendMessage);
    }

    private void cancelHandler(BaseAbilityBot baseAbilityBot, Update update) {
        long chatId = update.getMessage().getChatId();
        log.info("[ResponseHandler - cancelHandler] chatId: {}", chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Kết thúc!");
        registerStates.remove(chatId);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        sender.execute(sendMessage);
    }

    private void unexpectedMessage(BaseAbilityBot baseAbilityBot, Update update) {
        long chatId = update.getMessage().getChatId();
        log.info("[ResponseHandler - unexpectedMessage] chatId: {}", chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Lệnh không hợp lệ");
        sender.execute(sendMessage);
    }

    public boolean userIsActive(long chatId) {
        return registerStates.containsKey(chatId);
    }
}
