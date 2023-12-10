package ptit.gms.store.kafka;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ptit.gms.config.ConfigValue;
import ptit.gms.dto.event.KafkaEventLoadCell;
import ptit.gms.utils.JsonUtils;

@Slf4j
@Service
public class KafkaProducerService {
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ConfigValue configValue;

    public void publishEventLoadCell(KafkaEventLoadCell kafkaEventLoadCell){
        log.info("[KafkaProducerService - publishEventLoadCell] event: {}", kafkaEventLoadCell);

        kafkaTemplate.send(configValue.getTopicPublishEventLoadCell(), JsonUtils.marshalJsonAsPrettyString(kafkaEventLoadCell));
    }
}
