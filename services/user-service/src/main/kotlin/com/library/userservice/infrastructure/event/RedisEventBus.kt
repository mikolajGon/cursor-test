package com.library.userservice.infrastructure.event

import com.library.userservice.domain.event.EventBus
import kotlinx.coroutines.*
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub

class RedisEventBus(private val host: String, private val port: Int) : EventBus {
    private val jedis = Jedis(host, port)
    private val scope = CoroutineScope(Dispatchers.IO + Job())

    override fun publish(channel: String, message: String) {
        jedis.publish(channel, message)
    }

    override fun subscribe(channel: String, callback: (String) -> Unit) {
        scope.launch {
            val subscriber =
                    object : JedisPubSub() {
                        override fun onMessage(channel: String, message: String) {
                            callback(message)
                        }
                    }
            jedis.subscribe(subscriber, channel)
        }
    }
}
