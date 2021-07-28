package com.example.telegrambotapi.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Qualifier("redis")
public class RabbitmqConfig {
    public static final String QUEUE = "poll";
    public static final String STOPQUEUE = "stop";
    public static final String EXPIREDQUEUE = "expired";
    public static final String OFFERQUEUE = "offer";
    public static final String SELECTIONQUEUE = "selection";
    public static final String EXCHANGE = "tour_exchange";
    public static final String ROUTING_KEY = "tour_routingkey";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    @Bean
    public Queue stopQueue() {
        return new Queue(STOPQUEUE);
    }

    @Bean
    public Queue offerQueue() {
        return new Queue(OFFERQUEUE);
    }

    @Bean
    public Queue selectionQueue() {
        return new Queue(SELECTIONQUEUE);
    }

    @Bean
    public Queue expiredQueue() {
        return new Queue(EXPIREDQUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean("rabbitmq")
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}
