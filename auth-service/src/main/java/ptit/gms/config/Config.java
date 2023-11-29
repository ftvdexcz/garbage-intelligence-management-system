package ptit.gms.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class Config {
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

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.socket-timeout}")
    private int redisSocketTimeout;

    @Value("${redis.command-timeout}")
    private int redisCommandTimeout;

    @Value("${api.secret-key}")
    private String apiKey;
}
