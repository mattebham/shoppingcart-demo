package com.shopping.cart.demo.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.cart.demo.model.OrderInfo;
import com.shopping.cart.demo.model.OrderInfoList;
import com.shopping.cart.demo.model.ProductList;
import com.shopping.cart.demo.service.OrderService;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceRoute extends RouteBuilder {

    private final OrderService orderService;

    @Autowired
    public OrderServiceRoute(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void configure() {

        from("direct:orders-get")
                .tracing()
                .log("Message body is ${body}")
                .to("bean:orderService?method=findAllOrders")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .outputType(OrderInfoList.class);

        from("direct:order-products-get")
                .tracing()
                .log("Message body is ${body}")
                .to("bean:orderService?method=findProducts(${header.id})")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .outputType(ProductList.class);

        from("direct:order-create")
                .tracing()
                .log("Message body is ${body}")
                .process(exchange -> {
                    String orderData = exchange.getIn().getBody(String.class);
                    String orderId = orderService.createOrder(new ObjectMapper().readValue(orderData, OrderInfo.class));
                    exchange.getIn().setBody(orderId);
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
                .outputType(String.class);

        from("direct:order-update")
                .tracing()
                .log("Message body is ${body}")
                .process(exchange -> {
                    String orderData = exchange.getIn().getBody(String.class);
                    orderService.updateOrder(new ObjectMapper().readValue(orderData, OrderInfo.class));
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(204))
                .setBody(simple(null));

    }
}
