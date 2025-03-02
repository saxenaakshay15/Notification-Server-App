package notification_server.notification_server.consumer;

import lombok.extern.slf4j.Slf4j;
import notification_server.notification_server.service.SMSConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.UUID;

@Slf4j
@Service
@EnableKafka
public class SMSConsumer {
    @Autowired
    private SMSConsumerService smsConsumerService;

    @KafkaListener(topics = "topic5", groupId = "topic5_group")
    public void consume(String id, Acknowledgment acknowledgment){
        if (id == null){
            throw new InvalidParameterException("SMS id must not be null");
        }
        smsConsumerService.consume(id, acknowledgment);
    }
}
