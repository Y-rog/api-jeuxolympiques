package com.yrog.apijeuxolympiques.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration de RabbitMQ.
 * Définit la queue, l'exchange et le binding pour la messagerie asynchrone.
 */
@Configuration
public class RabbitConfig {

    public static final String CART_ITEM_QUEUE = "cartItemQueue";
    public static final String CART_ITEM_EXCHANGE = "cartItemExchange";
    public static final String CART_ITEM_ROUTING_KEY = "cartitem.#";

    /**
     * Queue durable pour les articles du panier.
     * durable = true : la queue survit au redémarrage de RabbitMQ.
     */
    @Bean
    public Queue cartItemQueue() {
        return new Queue(CART_ITEM_QUEUE, true);
    }

    /**
     * Exchange de type Topic pour le routage des messages.
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(CART_ITEM_EXCHANGE);
    }

    /**
     * Binding entre la queue et l'exchange.
     */
    @Bean
    public Binding binding(Queue cartItemQueue, TopicExchange exchange) {
        return BindingBuilder.bind(cartItemQueue).to(exchange).with(CART_ITEM_ROUTING_KEY);
    }

    /**
     * Convertisseur de messages JSON pour RabbitMQ.
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}




