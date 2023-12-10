package gms.ptit.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ConfigValue {
    @Value("${env}")
    private String env;

    @Value("${mysql.datasource.driver-class-name}")
    private String mysqlClassName;

    @Value("${mysql.datasource.url}")
    private String mysqlUrl;

    @Value("${mysql.datasource.username}")
    private String mysqlUsername;

    @Value("${mysql.datasource.password}")
    private String mysqlPassword;

    @Value(value = "${spring.kafka.bootstrap-address}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.properties.sasl.jaas.config}")
    private String kafkaSaslJaasConfig;

    @Value(value = "${spring.kafka.properties.sasl.mechanism}")
    private String kafkaSaslMechanism;

    @Value(value = "${spring.kafka.properties.security.protocol}")
    private String kafkaSecurityProtocol;

    @Value(value = "${telegram.bot.token}")
    private String telegramBotToken;

    @Value(value = "${kafka.topic.subscribe.event.load-cell}")
    private String topicSubscribeEventLoadCell;
}
