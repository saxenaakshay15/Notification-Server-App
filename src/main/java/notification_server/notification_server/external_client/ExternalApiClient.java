package notification_server.notification_server.external_client;

import notification_server.notification_server.entity.SMSBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "externalApi", url = "https://api.imiconnect.in/resources/v1/messaging")
public interface ExternalApiClient {
    @PostMapping(headers = "key = 93ceffda-5941-11ea-9da9-025282c394f2")
    ResponseEntity<String> sendData(@RequestBody SMSBody body);
}
