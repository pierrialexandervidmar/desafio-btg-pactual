package com.btgpactual.order_ms.listener;

import com.btgpactual.order_ms.listener.dto.OrderCreateEventDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.btgpactual.order_ms.config.RabbitMqConfig.ORDER_CREATED_QUEUE;

@Component
public class OrderCreatedListener {

    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void listen(Message<OrderCreateEventDto> message) {
        message.g
    }
}
