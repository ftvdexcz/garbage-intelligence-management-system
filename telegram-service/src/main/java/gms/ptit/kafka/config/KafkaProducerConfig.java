package gms.ptit.kafka.config;

import gms.ptit.config.ConfigValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaProducerConfig {
    @Autowired
    ConfigValue configValue;

    @Bean
    public ProducerFactory<String, String> producerFactory(){
        Map<String, Object> props = new HashMap<>();
        log.info("address: {}", configValue.getBootstrapAddress());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configValue.getBootstrapAddress());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(SaslConfigs.SASL_MECHANISM, configValue.getKafkaSaslMechanism());
        props.put(SaslConfigs.SASL_JAAS_CONFIG, configValue.getKafkaSaslJaasConfig());
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, configValue.getKafkaSecurityProtocol());
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateMessageCreateRequest() {
        return new KafkaTemplate<>(producerFactory());
    }
}