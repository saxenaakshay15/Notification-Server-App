package notification_server.notification_server.service;

import notification_server.notification_server.repository.SMSElasRepositoryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import notification_server.notification_server.entity.SMSElas;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

@Service
public class SMSElasService {
    @Autowired
    private SMSElasRepositoryHandler smselasRepositoryHandler;

    public SMSElas save(@RequestBody SMSElas smselas) {
        return smselasRepositoryHandler.save(smselas);
    }

    public List<SMSElas> getAll() {
        return smselasRepositoryHandler.findAll();
    }

    public void deleteAll() {
        smselasRepositoryHandler.deleteAll();
    }

    public List<SMSElas> findByCreatedAtBetween(String startTimeString, String endTimeString, int pageNumber, int pageSize) {
        startTimeString = startTimeString.replace(" ", "+");
        endTimeString = endTimeString.replace(" ", "+");

        OffsetDateTime startTime = OffsetDateTime.parse(startTimeString);
        OffsetDateTime endTime = OffsetDateTime.parse(endTimeString);

        Date startLocalDateTime = Date.from(startTime.toInstant());
        Date endLocalDateTime = Date.from(endTime.toInstant());

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<SMSElas> result = smselasRepositoryHandler.findByCreatedAtBetween(startLocalDateTime, endLocalDateTime, pageable);

        return result.getContent();
    }

    public List<SMSElas> searchByText(String text, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<SMSElas> result = smselasRepositoryHandler.findByMessageContaining(text, pageable);

        return result.getContent();
    }
}