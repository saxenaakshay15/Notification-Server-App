package notification_server.notification_server.service;

import notification_server.notification_server.entity.SMS;
import notification_server.notification_server.entity.SMSRepositoryResponse;
import notification_server.notification_server.repository.SMSRepositoryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SMSRepositoryService {
    @Autowired
    SMSRepositoryHandler smsRepositoryHandler;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public List<SMS> getAllSMS() {
        return smsRepositoryHandler.findAll();
    }

    public Optional<SMS> findByID(String id){
        return smsRepositoryHandler.findById(UUID.fromString(id));
    }

    public SMSRepositoryResponse createSMS(SMS sms) {
        sms.setCreatedAt(LocalDateTime.now());
        sms.setUpdatedAt(LocalDateTime.now());
        smsRepositoryHandler.save(sms);
        SMSRepositoryResponse smsRepositoryResponse = new SMSRepositoryResponse("Successful", "Message sent succesfully", sms.getId().toString(), "NA");
        kafkaTemplate.send("topic5", String.valueOf(sms.getId()));
        return smsRepositoryResponse;
    }

    public SMSRepositoryResponse failedResponse(String errorMessage){
        return new SMSRepositoryResponse("Unsuccessful", errorMessage, "NA", errorMessage);
    }
}
