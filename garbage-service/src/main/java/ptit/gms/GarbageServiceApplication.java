package ptit.gms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackages = {"ptit.gms.store.mysql.entity"})
@EnableJpaRepositories(basePackages = {"ptit.gms.store.mysql.repository"})
@ServletComponentScan
@EnableScheduling
public class GarbageServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GarbageServiceApplication.class, args);
	}

}
