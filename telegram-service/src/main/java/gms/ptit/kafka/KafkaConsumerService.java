package gms.ptit.kafka;

import gms.ptit.config.ConfigValue;
import gms.ptit.constant.EventType;
import gms.ptit.dto.event.KafkaEventLoadCell;
import gms.ptit.mysql.entity.UserChatInfoEntity;
import gms.ptit.service.UserChatInfoService;
import gms.ptit.service.bot.BotPolling;
import gms.ptit.utils.GenerateTemplateUtils;
import gms.ptit.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

import java.util.List;

@Slf4j
@Service
public class KafkaConsumerService {
    @Autowired
    ConfigValue configValue;

    @Autowired
    BotPolling botPolling;

    @Autowired
    UserChatInfoService userChatInfoService;

    @KafkaListener(topics = "${kafka.topic.subscribe.event.load-cell}", groupId = "${kafka.group.id.subscribe.event.load-cell}",
            properties = {
                    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG + "=earliest",
            })
    public void listenLoadCellEvent(String event) throws TelegramApiException {
        log.info("[KafkaConsumerService - listenLoadCellEvent] Received event load cell: {}" + event);
        KafkaEventLoadCell loadCellEvent = JsonUtils.unJson(event, KafkaEventLoadCell.class);
        if(loadCellEvent != null){
            List<UserChatInfoEntity> users = userChatInfoService.findAllByTypeIn(List.of(EventType.EVENT_TYPE_LOAD_CELL.code,
                                                        EventType.EVENT_TYPE_ALL.code));

            String html = GenerateTemplateUtils.generateLoadCellEventTemplate(loadCellEvent);
            SendMessage message = new SendMessage();
            message.setText(html);
            message.setParseMode(ParseMode.HTML);
            message.disableWebPagePreview();

            for(UserChatInfoEntity u: users){
                message.setChatId(u.getChatId());
                botPolling.executeAsync(message, new SentCallback<Message>() {
                    @Override
                    public void onResult(BotApiMethod<Message> botApiMethod, Message message) {
                        log.info("[KafkaConsumerService - onResult] send message success");
                    }

                    @Override
                    public void onError(BotApiMethod<Message> botApiMethod, TelegramApiRequestException e) {
                        log.info("[KafkaConsumerService - onResult] send message error: {}", e.getMessage());
                    }

                    @Override
                    public void onException(BotApiMethod<Message> botApiMethod, Exception e) {
                        log.info("[KafkaConsumerService - onResult] send message exception: {}", e.getMessage());
                    }
                });
            }
        }
    }
}
