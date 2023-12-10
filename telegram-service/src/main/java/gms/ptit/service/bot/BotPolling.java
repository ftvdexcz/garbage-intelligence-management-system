package gms.ptit.service.bot;

import gms.ptit.config.ConfigValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.ADMIN;

@Slf4j
@Component
public class BotPolling extends AbilityBot{
    @Autowired
    ConfigValue configValue;

    public BotPolling(ConfigValue configValue) {
        super(configValue.getTelegramBotToken(), "bot");
    }

    public Ability sayHelloWorld() {
        return Ability
                .builder()
                .name("hello")
                .info("says hello world!")
                .input(0)
                .locality(USER)
                .privacy(ADMIN)
                .action(ctx -> log.info("hello"))
                .build();
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        Update update = updates.get(0);
        ChatMemberUpdated chatMemberUpdated = update.getMyChatMember();
        if(chatMemberUpdated != null){
            long chatId = chatMemberUpdated.getChat().getId();
            log.info("chatId: {}", chatId);
            log.info("oldChatMember: {}", chatMemberUpdated.getOldChatMember().getStatus()); // member
            log.info("newChatMember: {}", chatMemberUpdated.getNewChatMember().getStatus()); // left : roi khoi nhom
        }
    }

    @Override
    public long creatorId() {
        return 1742013321;
    }
}
