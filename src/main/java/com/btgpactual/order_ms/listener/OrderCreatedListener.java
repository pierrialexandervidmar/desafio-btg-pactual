package com.btgpactual.order_ms.listener;

import com.btgpactual.order_ms.listener.dto.OrderCreateEventDto;
import com.btgpactual.order_ms.services.OrderService;
import com.rabbitmq.client.AMQP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.btgpactual.order_ms.config.RabbitMqConfig.ORDER_CREATED_QUEUE;

/**
 * Componente responsável por escutar mensagens da fila de criação de pedidos no RabbitMQ.
 *
 * <p>
 * Esta classe implementa um listener que consome mensagens enviadas para a fila
 * {@code btg-pactual-order-created} e registra as informações no log.
 * </p>
 */
@Component
public class OrderCreatedListener {

    /**
     * Logger utilizado para registrar informações sobre as mensagens consumidas.
     */
    private final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final OrderService orderService;

    public OrderCreatedListener(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Metodo que escuta e consome mensagens da fila {@code btg-pactual-order-created}.
     *
     * <p>
     * Este metodo é automaticamente invocado sempre que uma nova mensagem do tipo
     * {@link OrderCreateEventDto} for recebida na fila. As mensagens consumidas são registradas no log.
     * </p>
     *
     * @param message a mensagem recebida do RabbitMQ contendo o payload do evento de criação de pedido.
     */
    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void listen(Message<OrderCreateEventDto> message) {
        logger.info("Message consumed: {}", message);

        orderService.save(message.getPayload());
    }
}
