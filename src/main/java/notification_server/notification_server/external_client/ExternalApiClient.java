package notification_server.notification_server.external_client;

import notification_server.notification_server.entity.SMSBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "externalApi", url = "URL")
public interface ExternalApiClient {
    @PostMapping(headers = "key = KEY")
    ResponseEntity<String> sendData(@RequestBody SMSBody body);
}
