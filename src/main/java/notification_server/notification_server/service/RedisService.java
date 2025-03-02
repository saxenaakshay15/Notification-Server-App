package notification_server.notification_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final Jedis jedis;

    public RedisService(){
        jedis = new Jedis("localhost", 6379);
    }

    public RedisService(Jedis jedis) {
        this.jedis = jedis;
    }

    public RedisService(RedisTemplate<String, String> redisTemplate, Jedis mockJedis) {
        this.redisTemplate = redisTemplate;
        this.jedis = mockJedis;
    }

    public ArrayList<String> getAll() {
        Set<String> keys = jedis.keys("*");
        return new ArrayList<>(keys);
    }

    public boolean phoneNumberExists(String phoneNumber) {
        return jedis.exists(phoneNumber);
    }

    public void set(String key, String value, Long ttl) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {

        }
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }
}
