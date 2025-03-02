package notification_server.notification_server.repository;

import notification_server.notification_server.entity.SMSElas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public interface SMSElasReadRepository extends ElasticsearchRepository<SMSElas, UUID> {
    List<SMSElas> findAll();
    Page<SMSElas> findByCreatedAtBetween(Date startTime, Date endTime, Pageable pageable);
    @Query("{\"match\": {\"message\": \"?0\"}}")
    Page<SMSElas> findByMessageContaining(String message, Pageable pageable);
}