package ptit.gms.store.redis.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Repository
public class RedisRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    public Object getKeyValue(Object key) {
        return redisTemplate.opsForValue().get(key);
    }

    public long getKeyExpire(Object key){
        // Get the remaining time to live for the key in seconds
        return redisTemplate.getExpire(key);
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public void setKeyValue(Object key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setKeyValueExpire(Object key, Object value, Long expireMs) {
        redisTemplate.opsForValue().set(key, value, Duration.ofMillis(expireMs));
    }

    public List<Object> getListValue(String name, long start, long end) {
        return redisTemplate.opsForList().range(name, start, end);
    }

    public List<Object> getListValue(String name) {
        return redisTemplate.opsForList().range(name, 0, -1);
    }

    public void pushListValue(String name, List<Object> listData) {
        redisTemplate.opsForList().rightPushAll(name, listData);
    }

    public void pushListValue(String name, Object data) {
        redisTemplate.opsForList().rightPush(name, data);
    }

    public void removeKey(String name) {
        redisTemplate.delete(name);
    }

    public List<String> listAllKeys(String namePattern) {
        List<String> result = new ArrayList<>();

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Set<String> keyLists = redisTemplate.keys("*" + namePattern + "*");
        Iterator<String> it = keyLists.iterator();
        while (it.hasNext()) {
            String data = it.next();
            data = data
                    .replace("\ufffd", "")
                    .replace("\ufffd", "")
                    .replace("\u0005t", "")
                    .replace("\u00005", "")
                    .replace("\u00004", "")
                    .replace("\u00000", "")
                    .replace("\u00003", "")
                    .replace("\u0001", "")
                    .replace("\u0000", "");
            result.add(data);
        }

        return result;
    }

    public boolean checkKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public String getType(Object key) {
        return redisTemplate.type(key).name();
    }
}

