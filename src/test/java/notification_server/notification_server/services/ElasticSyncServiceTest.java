package notification_server.notification_server.services;

import static org.mockito.Mockito.*;
import notification_server.notification_server.entity.SMS;
import notification_server.notification_server.entity.SMSElas;
import notification_server.notification_server.health_check.ElasticHealthCheck;
import notification_server.notification_server.repository.SMSElasRepositoryHandler;
import notification_server.notification_server.repository.SMSRepositoryHandler;
import notification_server.notification_server.service.ElasticSyncService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ElasticSyncServiceTest {

    @Mock
    private SMSRepositoryHandler smsRepositoryHandler;

    @Mock
    private SMSElasRepositoryHandler smsElasRepositoryHandler;

    @Mock
    private ElasticHealthCheck elasticHealthCheck;

    @InjectMocks
    private ElasticSyncService elasticSyncService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSyncDataToElasticsearch_WhenElasticsearchIsUp() {
        when(elasticHealthCheck.isElasticsearchUp()).thenReturn(true);
        SMS sms1 = new SMS();
        sms1.setCreatedAt(LocalDateTime.now());
        sms1.setUpdatedAt(LocalDateTime.now());
        SMS sms2 = new SMS();
        sms2.setCreatedAt(LocalDateTime.now());
        sms2.setUpdatedAt(LocalDateTime.now());
        List<SMS> smsList = Arrays.asList(
                sms1, sms2
        );
        when(smsRepositoryHandler.findAll()).thenReturn(smsList);

        elasticSyncService.syncDataToElasticsearch();

        verify(smsElasRepositoryHandler, times(smsList.size())).save(any(SMSElas.class));
    }

    @Test
    void testSyncDataToElasticsearch_WhenElasticsearchIsDown() {
        when(elasticHealthCheck.isElasticsearchUp()).thenReturn(false);

        elasticSyncService.syncDataToElasticsearch();

        verify(smsRepositoryHandler, never()).findAll();
        verify(smsElasRepositoryHandler, never()).save(any());
    }
}

