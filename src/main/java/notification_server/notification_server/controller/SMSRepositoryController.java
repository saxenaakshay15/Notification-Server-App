package notification_server.notification_server.controller;

import lombok.extern.slf4j.Slf4j;
import notification_server.notification_server.entity.SMS;
import notification_server.notification_server.entity.SMSRepositoryResponse;
import notification_server.notification_server.exception.InvalidPhoneNumberException;
import notification_server.notification_server.exception.InvalidSMSMessageException;
import notification_server.notification_server.service.SMSRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static notification_server.notification_server.validators.ValidatorChecker.validatePhoneNumber;
import static notification_server.notification_server.validators.ValidatorChecker.validateSMSMessage;

@RestController
@RequestMapping("/v1/sms/send")
@Slf4j
public class SMSRepositoryController {
    @Autowired
    SMSRepositoryService smsRepositoryService;

    @GetMapping("/get-all")
    public ResponseEntity<List<SMS>> getAll(){
        if(smsRepositoryService.getAllSMS().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(smsRepositoryService.getAllSMS(), HttpStatus.OK);
    }

    @GetMapping("get")
    public ResponseEntity<SMS> findByID(@RequestParam("id") String id){
        Optional<SMS> sms = smsRepositoryService.findByID(id);
        if(sms.isPresent()){
            return new ResponseEntity<>(sms.get(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<SMSRepositoryResponse> create(@RequestBody SMS sms) {
        try {
            validatePhoneNumber(sms.getPhoneNumber());
            validateSMSMessage(sms.getMessage());

            return ResponseEntity.status(HttpStatus.CREATED).body(smsRepositoryService.createSMS(sms));

        } catch (InvalidPhoneNumberException | InvalidSMSMessageException ex) {
            SMSRepositoryResponse smsRepositoryResponse = smsRepositoryService.failedResponse(ex.getMessage());
            log.error("Validation error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(smsRepositoryResponse);
        }
    }
}
