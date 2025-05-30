package com.btgpactual.order_ms.services;

import com.btgpactual.order_ms.entities.Order;
import com.btgpactual.order_ms.entities.OrderItem;
import com.btgpactual.order_ms.listener.dto.OrderCreateEventDto;
import com.btgpactual.order_ms.repositories.OrderRepository;
import com.btgpactual.order_ms.resources.dto.OrderResponse;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void save(OrderCreateEventDto eventDto) {
        var entity = new Order();

        entity.setOrderId(eventDto.codigoPedido());
        entity.setCustomerId(eventDto.codigoCliente());
        entity.setItens(getOrderIems(eventDto));
        entity.setTotal(getTotal(eventDto));

        orderRepository.save(entity);
    }

    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest) {
        var order = orderRepository.findAllByCustomerId(customerId, pageRequest);
        return order.map(OrderResponse::fromEntity);
    }

    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId) {
        var aggregations = newAggregation(
            match(Criteria.where("customerId").is(customerId)),
            group().sum("total").as("total")
        );

        var response = mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);

        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
    }

    private BigDecimal getTotal(OrderCreateEventDto eventDto) {
        return eventDto.itens()
                .stream()
                .map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private static List<OrderItem> getOrderIems(OrderCreateEventDto eventDto) {
        return eventDto.itens().stream()
                .map(i -> new OrderItem(i.produto(), i.quantidade(), i.preco()))
                .toList();
    }
}
