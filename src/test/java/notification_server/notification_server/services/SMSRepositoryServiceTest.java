package notification_server.notification_server.services;

import notification_server.notification_server.entity.SMS;
import notification_server.notification_server.repository.SMSRepositoryHandler;
import notification_server.notification_server.service.SMSRepositoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SMSRepositoryServiceTest {
    @Mock
    private SMSRepositoryHandler smsRepositoryHandler;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private SMSRepositoryService smsRepositoryService;

    @Test
    public void testCreateSMSWhenNotExists() {
        SMS sms = new SMS();
        sms.setId(UUID.randomUUID());

        smsRepositoryService.createSMS(sms);

        verify(smsRepositoryHandler).save(sms);
        verify(kafkaTemplate).send("topic5", sms.getId().toString());
    }

    @Test
    public void testFindByID(){
        SMS sms = new SMS();
        UUID id = UUID.randomUUID();
        sms.setId(id);

        smsRepositoryService.createSMS(sms);
        when(smsRepositoryHandler.findById(id)).thenReturn(Optional.of(sms));
        assertEquals(sms, smsRepositoryService.findByID(id.toString()).get());
    }

    @Test
    public void testGetAllSMSs() {
        SMS sms1 = new SMS();
        sms1.setId(UUID.randomUUID());
        smsRepositoryService.createSMS(sms1);

        SMS sms2 = new SMS();
        sms2.setId(UUID.randomUUID());
        smsRepositoryService.createSMS(sms2);

        when(smsRepositoryHandler.findAll()).thenReturn(new ArrayList<>(Arrays.asList(sms1, sms2)));

        List<SMS> smsList = smsRepositoryService.getAllSMS();
        assertEquals(smsList.get(0), sms1);
        assertEquals(smsList.get(1), sms2);
    }
}
