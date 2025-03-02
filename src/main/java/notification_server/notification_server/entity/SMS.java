package notification_server.notification_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table (name = "sms_new")
public class SMS {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private String phoneNumber;
    @Column
    private String message;
    @Column
    private String status;
    @Column
    private String failureCode;
    @Column
    private String failureComments;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;
}
