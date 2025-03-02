package notification_server.notification_server.controller;

import lombok.extern.slf4j.Slf4j;
import notification_server.notification_server.entity.SMSElas;
import notification_server.notification_server.exception.InvalidPhoneNumberException;
import notification_server.notification_server.exception.InvalidSMSMessageException;
import notification_server.notification_server.exception.InvalidTextException;
import notification_server.notification_server.exception.InvalidTimeStringException;
import notification_server.notification_server.service.SMSElasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static notification_server.notification_server.validators.ValidatorChecker.*;

@RestController
@RequestMapping("/elas")
@Slf4j
public class SMSElasController {
    @Autowired
    private SMSElasService smselasService;

    @GetMapping("/get-all")
    public ResponseEntity<List<SMSElas>> getAll(){
        if(smselasService.getAll().isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(smselasService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/save")
    ResponseEntity<SMSElas> save(@RequestBody SMSElas smselas) {
        try {
            validatePhoneNumber(smselas.getPhoneNumber());
            validateSMSMessage(smselas.getMessage());
            smselasService.save(smselas);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);

        } catch (InvalidPhoneNumberException | InvalidSMSMessageException ex) {
            log.error("Validation error while saving SMSElas: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/query")
    public ResponseEntity<List<SMSElas>> searchByStartTimeAndEndTime(
            @RequestParam("StartTime") String startTimeString,
            @RequestParam("EndTime") String endTimeString,
            @RequestParam("PageNumber") int pageNumber,
            @RequestParam("PageSize") int pageSize) {

        try {
            validateTimeString(startTimeString, "StartTime");
            validateTimeString(endTimeString, "EndTime");

            List<SMSElas> smsElasList = smselasService.findByCreatedAtBetween(startTimeString, endTimeString, pageNumber, pageSize);
            if (smsElasList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(smsElasList, HttpStatus.OK);

        } catch (InvalidTimeStringException ex) {
            log.error("Validation error while extracting times: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<SMSElas>> searchByText(
            @RequestParam("text") String text,
            @RequestParam("PageNumber") int pageNumber,
            @RequestParam("PageSize") int pageSize) {
        try{
            validateText(text);
            List<SMSElas> smsList = smselasService.searchByText(text, pageNumber, pageSize);
            if (smsList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(smsList, HttpStatus.OK);
        } catch (InvalidTextException e) {
            log.error("Given text must not be empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
