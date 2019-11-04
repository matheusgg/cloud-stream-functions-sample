package br.com.sample

import br.com.sample.model.Product
import br.com.sample.model.User
import com.rabbitmq.client.Channel
import mu.KLogger
import mu.KotlinLogging.logger
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.function.context.PollableBean
import org.springframework.context.annotation.Bean
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import reactor.core.publisher.Flux.merge
import reactor.util.function.Tuple2
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier
import kotlin.random.Random.Default.nextInt

@SpringBootApplication
class CloudStreamFunctionsSampleApplication(private val log: KLogger = logger { }) {

    @Bean
    fun handleUser() = Consumer<User> { log.info { "==================> User: $it" } }

    @Bean
    fun handleProduct() = Consumer<Product> { log.info { "==================> Product: $it" } }

    @Bean
    fun trim() = Function<String, String> {
        log.info { "==================> trim: $it" }
        it.trim()
    }

    @Bean
    fun toUpperCase() = Function<String, String> {
        log.info { "==================> toUpperCase: $it" }
        it.toUpperCase()
    }

    @Bean
    fun consumeString() = Consumer<String> { log.info { "==================> String: $it" } }

    @Bean
    fun handleItems() = Function<Tuple2<Flux<String>, Flux<Int>>, Flux<String>> {
        val strings = it.t1
        val ints = it.t2.map { item -> item.toString() }
        merge(strings, ints)
    }

    @Bean
    fun handleUserManual() = Consumer<Message<User>> {
        log.info { "==================> User Manual: ${it.payload}" }
        val channel = it.headers[AmqpHeaders.CHANNEL] as Channel
        val tag = it.headers[AmqpHeaders.DELIVERY_TAG] as Long
        channel.basicAck(tag, false)
    }

    @PollableBean
    fun stringSupplier() = Supplier { "Some message ${nextInt()}" }

    @Bean
    fun simpleConsumer1() = Consumer<String> {
        log.info { "==================> Consumer1: $it" }
    }

    @Bean
    fun simpleConsumer2() = Consumer<String> {
        log.info { "==================> Consumer2: $it" }
    }
}

fun main(args: Array<String>) {
    runApplication<CloudStreamFunctionsSampleApplication>(*args)
}
