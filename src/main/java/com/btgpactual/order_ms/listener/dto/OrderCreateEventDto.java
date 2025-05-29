package com.btgpactual.order_ms.listener.dto;

import java.util.List;

public record OrderCreateEventDto(
        Long codigoPedido,
        Long codigoCliente,
        List<OrderItemEvent> itens
) {
}
