package notification_server.notification_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DestinationInfo {
    private ArrayList<String> msisdn;
    private String correlationId;
}
