package notification_server.notification_server.services;

import notification_server.notification_server.entity.*;
import notification_server.notification_server.external_client.ExternalApiClient;
import notification_server.notification_server.repository.SMSRepositoryHandler;
import notification_server.notification_server.service.RedisService;
import notification_server.notification_server.service.SMSConsumerService;
import notification_server.notification_server.service.SMSElasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class SMSConsumerServiceTest {
    @Mock
    private SMSRepositoryHandler smsRepositoryHandler;

    @Mock
    private RedisService redisService;

    @Mock
    private SMSElasService smsElasService;

    @Mock
    private ExternalApiClient externalApiClient;

    @Mock
    private Acknowledgment acknowledgment;

    @InjectMocks
    private SMSConsumerService smsConsumerService;

    private SMS mockSms;
    private UUID id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();

        mockSms = new SMS();
        mockSms.setId(id);
        mockSms.setPhoneNumber("1234567890");
        mockSms.setMessage("Test message");
        mockSms.setCreatedAt(LocalDateTime.now());
        mockSms.setUpdatedAt(LocalDateTime.now());

        when(smsRepositoryHandler.findById(id)).thenReturn(Optional.ofNullable(mockSms));
        smsConsumerService = new SMSConsumerService(smsRepositoryHandler, externalApiClient, redisService, smsElasService);
    }

    @Test
    void testConsume_BlacklistScenario() {
        when(redisService.phoneNumberExists(any(String.class))).thenReturn(true);
        smsConsumerService.consume(String.valueOf(id), acknowledgment);

        verify(smsRepositoryHandler, times(1)).save(any(SMS.class));
    }


    @Test
    void testConsume_SuccessScenario() {
        when(redisService.phoneNumberExists(any(String.class))).thenReturn(false);

        smsConsumerService.consume(String.valueOf(id), acknowledgment);

        verify(smsElasService, times(1)).save(any(SMSElas.class));
        verify(acknowledgment, times(1)).acknowledge();
        verify(smsRepositoryHandler, times(1)).save(any(SMS.class));
    }

    @Test
    void testConsume_BadGatewayScenario() {
        when(externalApiClient.sendData(any(SMSBody.class))).thenThrow(new RuntimeException());
        when(redisService.phoneNumberExists(any(String.class))).thenReturn(false);

        smsConsumerService.consume(String.valueOf(id), acknowledgment);

        verify(smsElasService, times(1)).save(any(SMSElas.class));
    }
}
