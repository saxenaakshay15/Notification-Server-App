package notification_server.notification_server.repository;

import notification_server.notification_server.entity.SMS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SMSWriteRepository extends JpaRepository<SMS, UUID> {
}
