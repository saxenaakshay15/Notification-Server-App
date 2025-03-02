package notification_server.notification_server.health_check;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
    @GetMapping("health")
    public String healthCheck() {
        return "OK";
    }
}
