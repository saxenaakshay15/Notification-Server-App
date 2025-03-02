package notification_server.notification_server.repository;

import notification_server.notification_server.entity.SMS;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SMSRepositoryHandler extends SMSReadRepository, SMSWriteRepository {
    @Override
    public Optional<SMS> findById(UUID id);
}
