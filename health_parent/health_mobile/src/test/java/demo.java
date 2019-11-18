import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:spring-redis.xml"})
public class demo {
    @Autowired
    private JedisPool jedisPool;
    @Test
    public void f1(){
        System.out.println(jedisPool);
        Jedis jedis = jedisPool.getResource();
        jedis.setex("test",60*60*5,"hello");
        String test = jedis.get("test");
        System.out.println(test);
        jedis.close();
    }
}
