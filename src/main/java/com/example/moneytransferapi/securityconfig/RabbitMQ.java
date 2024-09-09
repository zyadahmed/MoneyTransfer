package com.example.moneytransferapi.securityconfig;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQ {

    @Bean
    public Queue queue(){
        return new Queue("messageNotificationQueue");
    }


    @Bean
    public TopicExchange exchange(){
        return new TopicExchange("notification_exchange");
    }


    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue())
                .to(exchange()).with("notification_routing");
    }
    @Bean
    public
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

}
