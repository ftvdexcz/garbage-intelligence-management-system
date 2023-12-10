package gms.ptit.service.bot;

import gms.ptit.config.ConfigValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.function.BiConsumer;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

@Slf4j
@Component
public class BotPolling extends AbilityBot{
    ResponseHandler responseHandler;



    @Autowired
    public BotPolling(ConfigValue configValue, ResponseHandler responseHandler) {
        super(configValue.getTelegramBotToken(), "bot");
        this.responseHandler = responseHandler;
        this.responseHandler.setSenderAndDBState(silent, db);
    }

    public Ability hello() {
        return Ability
                .builder()
                .name("hello")
                .info("subscribe with chat id")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> log.info("hello"))
                .build();
    }

    public Ability subscribe() {
        return Ability
                .builder()
                .name("subscribe")
                .info("subscribe with chat id")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> responseHandler.subscribeHandler(ctx))
                .build();
    }

    public Ability unsubscribe() {
        return Ability
                .builder()
                .name("unsubscribe")
                .info("unsubscribe event")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> responseHandler.unsubscribeHandler(ctx))
                .build();
    }

    @Override
    public void onUpdateReceived(Update update) {
        super.onUpdateReceived(update);
        ChatMemberUpdated chatMemberUpdated = update.getMyChatMember();
        if(chatMemberUpdated != null){
            long chatId = chatMemberUpdated.getChat().getId();
            log.info("chatId: {}", chatId);
            log.info("oldChatMember: {}", chatMemberUpdated.getOldChatMember().getStatus()); // member
            log.info("newChatMember: {}", chatMemberUpdated.getNewChatMember().getStatus()); // left : roi khoi nhom

            if(chatMemberUpdated.getNewChatMember().getStatus().equalsIgnoreCase("left")){
                responseHandler.leftGroupHandler(chatId);
            }else if(chatMemberUpdated.getNewChatMember().getStatus().equalsIgnoreCase("member")){
                responseHandler.joinGroupHandler(chatId);
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Xin chào!");
                try {
                    sender.execute(message);
                } catch (TelegramApiException e) {
                    log.info("onUpdateReceived error: " + e.getMessage());
                }
            }

        }
    }

    public Reply replyHandler() {
        BiConsumer<BaseAbilityBot, Update> action = (abilityBot, update) -> responseHandler.replyHandler(abilityBot, update);
        return Reply.of(action, Flag.TEXT, update -> responseHandler.userIsActive(AbilityUtils.getChatId(update)));
    }

    @Override
    public long creatorId() {
        return 1742013321;
    }
}
