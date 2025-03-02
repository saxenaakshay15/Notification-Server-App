package notification_server.notification_server.service;

import lombok.extern.slf4j.Slf4j;
import notification_server.notification_server.entity.*;
import notification_server.notification_server.external_client.ExternalApiClient;
import notification_server.notification_server.repository.SMSRepositoryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@EnableKafka
public class SMSConsumerService {
    @Autowired
    private SMSRepositoryHandler smsRepositoryHandler;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SMSElasService smsElasService;

    private final ExternalApiClient externalApiClient;

    public SMSConsumerService(SMSRepositoryHandler smsRepositoryHandler, ExternalApiClient externalApiClient, RedisService redisService, SMSElasService smsElasService) {
        this.smsRepositoryHandler = smsRepositoryHandler;
        this.externalApiClient = externalApiClient;
        this.redisService = redisService;
        this.smsElasService = smsElasService;
    }

    public void consume(String id, Acknowledgment acknowledgment){
        SMS sms = smsRepositoryHandler.findById(UUID.fromString(id)).get();

        if(redisService.phoneNumberExists(sms.getPhoneNumber())){
            sms.setStatus("Blacklisted Phone Number");
            sms.setFailureComments("Blacklisted Phone Number");
            sms.setFailureCode("Blacklist");
            smsRepositoryHandler.save(sms);
            smsElasService.save(new SMSElas(sms, HttpStatus.FORBIDDEN.toString(), "Blacklisted PhoneNumber", "Blacklisted PhoneNumber"));
            log.info("Blacklisted phone number {}", sms.getPhoneNumber());
            return;
        }

        try{
            SMSBody smsBody = new SMSBody(sms);
            externalApiClient.sendData(smsBody);
            smsElasService.save(new SMSElas(sms, HttpStatus.OK.toString(), null, null));
            sms.setStatus(HttpStatus.OK.toString());
            smsRepositoryHandler.save(sms);
            acknowledgment.acknowledge();
        }
        catch (Exception exception){
            smsElasService.save(new SMSElas(sms, HttpStatus.BAD_GATEWAY.toString(), "502", "Bad Gateway"));
            sms.setStatus(HttpStatus.BAD_GATEWAY.toString());
            sms.setFailureCode("502");
            sms.setFailureComments("Bad Gateway");
            smsRepositoryHandler.save(sms);
            acknowledgment.nack(Duration.ofMinutes(1));
        }
    }
}
