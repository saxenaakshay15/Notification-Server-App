package notification_server.notification_server.service;

import lombok.extern.slf4j.Slf4j;
import notification_server.notification_server.entity.SMS;
import notification_server.notification_server.entity.SMSElas;
import notification_server.notification_server.health_check.ElasticHealthCheck;
import notification_server.notification_server.repository.SMSElasRepositoryHandler;
import notification_server.notification_server.repository.SMSRepositoryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class ElasticSyncService {
    @Autowired
    private SMSRepositoryHandler smsRepositoryHandler;

    @Autowired
    private SMSElasRepositoryHandler smsElasRepositoryHandler;

    @Autowired
    private ElasticHealthCheck elasticHealthCheck;

    public void syncDataToElasticsearch() {
        if (!elasticHealthCheck.isElasticsearchUp()) {
            log.info("Elasticsearch is DOWN. Data sync skipped.");
            return;
        }

        List<SMS> smsList = smsRepositoryHandler.findAll();
        for (SMS sms : smsList) {
            SMSElas smsElas = new SMSElas(sms);
            smsElasRepositoryHandler.save(smsElas);
        }

        log.info("Data successfully synced to Elasticsearch!");
    }
}
