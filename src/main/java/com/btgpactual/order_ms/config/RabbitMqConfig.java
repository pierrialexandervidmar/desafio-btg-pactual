package com.btgpactual.order_ms.config;

import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de componentes relacionados ao RabbitMQ para a aplicação.
 *
 * <p>
 * Esta classe define beans necessários para a serialização de mensagens no formato JSON
 * e para a declaração da fila utilizada para receber eventos de criação de pedidos.
 * </p>
 *
 * <p>
 * A fila configurada é chamada {@code btg-pactual-order-created}.
 * </p>
 */
@Configuration
public class RabbitMqConfig {

    /**
     * Nome da fila que será utilizada para eventos de criação de pedidos.
     */
    public static final String ORDER_CREATED_QUEUE = "btg-pactual-order-created";

    /**
     * Bean responsável por converter mensagens para o formato JSON e vice-versa.
     *
     * <p>
     * Este conversor será utilizado automaticamente pelo Spring AMQP para
     * serializar e desserializar mensagens enviadas e recebidas do RabbitMQ.
     * </p>
     *
     * @return uma instância de {@link Jackson2JsonMessageConverter}.
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Bean que declara a fila {@link #ORDER_CREATED_QUEUE} no RabbitMQ.
     *
     * <p>
     * A fila será criada automaticamente pelo Spring AMQP se ela ainda não existir
     * no broker do RabbitMQ.
     * </p>
     *
     * @return um {@link Declarable} representando a fila de criação de pedidos.
     */
    @Bean
    public Declarable orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE);
    }
}
