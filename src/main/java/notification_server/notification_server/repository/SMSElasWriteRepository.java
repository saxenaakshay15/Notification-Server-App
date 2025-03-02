package notification_server.notification_server.repository;

import notification_server.notification_server.entity.SMSElas;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface SMSElasWriteRepository extends ElasticsearchRepository<SMSElas, UUID> {
    void deleteAll();
}
