package notification_server.notification_server.scheduler;

import lombok.extern.slf4j.Slf4j;
import notification_server.notification_server.service.ElasticSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ElasticSyncScheduler {
    @Autowired
    private ElasticSyncService elasticSyncService;

    @Scheduled(fixedRate = 60000)
    public void checkAndSync() {
        log.info("Checking Elasticsearch health...");
        elasticSyncService.syncDataToElasticsearch();
    }
}
