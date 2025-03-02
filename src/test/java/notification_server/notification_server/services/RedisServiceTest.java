package notification_server.notification_server.services;

import notification_server.notification_server.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class RedisServiceTest {

    @InjectMocks
    private RedisService redisService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testGetAll() {
        Set<String> mockKeys = new HashSet<>();
        mockKeys.add("key1");
        mockKeys.add("key2");

        Jedis mockJedis = mock(Jedis.class);
        when(mockJedis.keys("*")).thenReturn(mockKeys);

        redisService = new RedisService(mockJedis);
        ArrayList<String> result = redisService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("key1"));
        assertTrue(result.contains("key2"));
    }

    @Test
    public void testSet() {
        String key = "testKey";
        String value = "testValue";
        Long ttl = 60L;
        Jedis mockJedis = mock(Jedis.class);

        redisService = new RedisService(redisTemplate, mockJedis);

        redisService.set(key, value, ttl);

        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOperations, times(1)).set(key, value, ttl, TimeUnit.SECONDS);
    }

    @Test
    public void testDelete() {
        String key = "testKey";
        when(redisTemplate.delete(any(String.class))).thenReturn(true);

        Boolean result = redisService.delete(key);

        assertTrue(result);
        verify(redisTemplate, times(1)).delete(key);
    }

    @Test
    public void testDelete_Failure() {
        String key = "testKey";
        when(redisTemplate.delete(any(String.class))).thenReturn(false);

        Boolean result = redisService.delete(key);

        assertFalse(result);
        verify(redisTemplate, times(1)).delete(key);
    }

    @Test
    public void testPhoneNumberExists() {
        String phoneNumber = "1234567890";

        Jedis mockJedis = mock(Jedis.class);
        when(mockJedis.exists(phoneNumber)).thenReturn(true);

        redisService = new RedisService(mockJedis);
        boolean result = redisService.phoneNumberExists(phoneNumber);
        assertTrue(result);
        verify(mockJedis, times(1)).exists(phoneNumber);
    }

    @Test
    public void testPhoneNumberDoesNotExist() {
        String phoneNumber = "0987654321";

        Jedis mockJedis = mock(Jedis.class);
        when(mockJedis.exists(phoneNumber)).thenReturn(false);

        redisService = new RedisService(mockJedis);
        boolean result = redisService.phoneNumberExists(phoneNumber);
        assertFalse(result);
        verify(mockJedis, times(1)).exists(phoneNumber);
    }

}
