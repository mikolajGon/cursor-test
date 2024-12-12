package com.library.bookservice.infrastructure.event

import com.library.bookservice.domain.event.EventBus
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub
import redis.clients.jedis.exceptions.JedisConnectionException

class RedisEventBus(private val host: String, private val port: Int) : EventBus {
    private val logger = LoggerFactory.getLogger(RedisEventBus::class.java)
    private var publisherJedis: Jedis? = null
    private var subscriberJedis: Jedis? = null
    private val scope = CoroutineScope(Dispatchers.IO + Job())

    private fun getPublisherJedis(): Jedis? {
        if (publisherJedis == null) {
            try {
                publisherJedis = Jedis(host, port)
            } catch (e: JedisConnectionException) {
                logger.warn("Failed to connect to Redis: ${e.message}")
            }
        }
        return publisherJedis
    }

    private fun getSubscriberJedis(): Jedis? {
        if (subscriberJedis == null) {
            try {
                subscriberJedis = Jedis(host, port)
            } catch (e: JedisConnectionException) {
                logger.warn("Failed to connect to Redis: ${e.message}")
            }
        }
        return subscriberJedis
    }

    override fun publish(channel: String, message: String) {
        try {
            getPublisherJedis()?.publish(channel, message)
        } catch (e: JedisConnectionException) {
            logger.error("Failed to publish message: ${e.message}")
        }
    }

    override fun subscribe(channel: String, callback: (String) -> Unit) {
        scope.launch {
            try {
                val subscriber =
                        object : JedisPubSub() {
                            override fun onMessage(channel: String, message: String) {
                                callback(message)
                            }
                        }
                getSubscriberJedis()?.subscribe(subscriber, channel)
            } catch (e: JedisConnectionException) {
                logger.error("Failed to subscribe to channel: ${e.message}")
            }
        }
    }
}
