package com.btgpactual.order_ms.services;

import com.btgpactual.order_ms.entities.Order;
import com.btgpactual.order_ms.entities.OrderItem;
import com.btgpactual.order_ms.listener.dto.OrderCreateEventDto;
import com.btgpactual.order_ms.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void save(OrderCreateEventDto eventDto) {
        var entity = new Order();

        entity.setOrderId(eventDto.codigoPedido());
        entity.setCustomerId(eventDto.codigoCliente());
        entity.setItems(getOrderIems(eventDto));
        entity.setTotal(getTotal(eventDto));

        orderRepository.save(entity);
    }

    private BigDecimal getTotal(OrderCreateEventDto eventDto) {
        return eventDto.items()
                .stream()
                .map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private static List<OrderItem> getOrderIems(OrderCreateEventDto eventDto) {
        return eventDto.items().stream()
                .map(i -> new OrderItem(i.produto(), i.quantidade(), i.preco()))
                .toList();
    }
}
