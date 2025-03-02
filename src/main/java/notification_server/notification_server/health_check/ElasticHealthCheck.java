package notification_server.notification_server.health_check;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class ElasticHealthCheck {

    private final RestTemplate restTemplate;

    public ElasticHealthCheck() {
        this.restTemplate = new RestTemplate();
    }

    public boolean isElasticsearchUp() {
        String elasticsearchUrl = "http://localhost:9200/_cluster/health";

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(elasticsearchUrl, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.out.println("Elasticsearch is DOWN: " + e.getMessage());
            return false;
        }
    }
}
