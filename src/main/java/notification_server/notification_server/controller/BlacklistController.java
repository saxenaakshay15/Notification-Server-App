package notification_server.notification_server.controller;

import lombok.extern.slf4j.Slf4j;
import notification_server.notification_server.entity.PhoneNumbers;
import notification_server.notification_server.exception.InvalidPhoneNumberException;
import notification_server.notification_server.service.RedisService;
import notification_server.notification_server.validators.ValidatorChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/blacklist")
@Slf4j
public class BlacklistController {

    private static final Logger logger = LoggerFactory.getLogger(BlacklistController.class);

    @Autowired
    private RedisService redisService;

    @PostMapping("/add")
    ResponseEntity<?> addPhoneNumber(@RequestBody PhoneNumbers phoneNumbers) {
        List<String> phoneNumberList = phoneNumbers.getPhoneNumbers();
        try {
            ValidatorChecker.validatePhoneNumberList(phoneNumberList);

            for (String phoneNumber : phoneNumberList) {
                ValidatorChecker.validatePhoneNumber(phoneNumber);
                redisService.set(phoneNumber, "True", 300L);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (InvalidPhoneNumberException ex) {
            logger.error("Validation error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/remove")
    ResponseEntity<?> removePhoneNumber(@RequestBody PhoneNumbers phoneNumbers) {
        List<String> phoneNumberList = phoneNumbers.getPhoneNumbers();
        try {
            ValidatorChecker.validatePhoneNumberList(phoneNumberList);

            boolean flag = false;
            for (String phoneNumber : phoneNumberList) {
                ValidatorChecker.validatePhoneNumber(phoneNumber);
                flag |= redisService.delete(phoneNumber);
            }

            if (flag) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (InvalidPhoneNumberException ex) {
            logger.error("Validation error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }


    @GetMapping("/get")
    ResponseEntity<ArrayList<String>> getPhoneNumbers() {
        if (redisService.getAll().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(redisService.getAll(), HttpStatus.OK);
    }
}
