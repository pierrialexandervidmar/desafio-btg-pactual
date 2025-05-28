package com.btgpactual.order_ms.repositories;

import com.btgpactual.order_ms.entities.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, Long> {

}
