package notification_server.notification_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SMSRepositoryResponse {
    private String status;
    private String message;
    private String SMSID;
    private String error;
}
