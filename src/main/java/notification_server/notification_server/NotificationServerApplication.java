package notification_server.notification_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan
@EnableJpaRepositories
@EnableFeignClients
@EnableElasticsearchRepositories
@EnableScheduling
public class NotificationServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(NotificationServerApplication.class, args);
	}
}
