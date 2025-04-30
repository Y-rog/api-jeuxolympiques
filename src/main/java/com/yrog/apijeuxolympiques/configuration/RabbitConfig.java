package com.yrog.apijeuxolympiques.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue cartItemQueue() {
        return new Queue("cartItemQueue", true);  // True = durable (persiste)
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("cartItemExchange");
    }

    @Bean
    public Binding binding(Queue cartItemQueue, TopicExchange exchange) {
        return BindingBuilder.bind(cartItemQueue).to(exchange).with("cartitem.#");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}




