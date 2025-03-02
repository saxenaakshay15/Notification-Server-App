package notification_server.notification_server.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(indexName = "sms_elastic_new")
public class SMSElas {
    @Field
    @Id
    private UUID id;
    @Field
    private String phoneNumber;
    @Field
    private String message;
    @Field
    private String status;
    @Field
    private String failureCode;
    @Field
    private String failureComments;
    @Field
    private Date createdAt;
    @Field
    private Date updatedAt;

    public SMSElas(SMS sms){
        this.phoneNumber = sms.getPhoneNumber();
        this.message = sms.getMessage();
        this.id = sms.getId();
        this.status = sms.getStatus();
        this.failureCode = sms.getFailureCode();
        this.failureComments = sms.getFailureComments();
        this.createdAt=Timestamp.valueOf(sms.getCreatedAt());
        this.updatedAt=Timestamp.valueOf(sms.getUpdatedAt());
    }

    public SMSElas(SMS sms, String status, String failureCode, String failureComments){
        this.setPhoneNumber(sms.getPhoneNumber());
        this.setMessage(sms.getMessage());
        this.setId(sms.getId());
        this.setCreatedAt(Timestamp.valueOf(sms.getCreatedAt()));
        this.setUpdatedAt(Timestamp.valueOf(sms.getUpdatedAt()));
        this.setStatus(status);
        this.setFailureCode(failureCode);
        this.setFailureComments(failureComments);
    }
}

