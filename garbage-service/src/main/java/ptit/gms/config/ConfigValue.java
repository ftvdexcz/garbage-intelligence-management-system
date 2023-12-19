package ptit.gms.config;

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

    @Value("${amazon.s3.url}")
    private String s3Url;

    @Value("${amazon.s3.bucket}")
    private String s3Bucket;

    @Value("${amazon.s3.region}")
    private String s3Region;

    @Value("${amazon.s3.access-key}")
    private String s3AccessKey;

    @Value("${amazon.s3.secret-key}")
    private String s3SecretKey;

    @Value(value = "${spring.kafka.bootstrap-address}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.properties.sasl.jaas.config}")
    private String kafkaSaslJaasConfig;

    @Value(value = "${spring.kafka.properties.sasl.mechanism}")
    private String kafkaSaslMechanism;

    @Value(value = "${spring.kafka.properties.security.protocol}")
    private String kafkaSecurityProtocol;

    @Value(value = "${kafka.topic.publish.event.load-cell}")
    private String topicPublishEventLoadCell;

    @Value(value = "${kafka.topic.publish.event.check-plate}")
    private String topicPublishEventCheckPlate;
}
