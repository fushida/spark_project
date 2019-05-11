import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.{JedisPool, JedisPoolConfig}

object JedisPools {

    private val jedisPool = new JedisPool(new GenericObjectPoolConfig(), "47.107.133.12", 6379)


    def getJedis() = jedisPool.getResource

}
/**
<dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>2.9.0</version>
        </dependency>

**/