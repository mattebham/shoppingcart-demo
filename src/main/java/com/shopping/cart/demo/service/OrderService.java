package com.shopping.cart.demo.service;

import com.shopping.cart.demo.exception.ProductNotFoundException;
import com.shopping.cart.demo.model.*;
import com.shopping.cart.demo.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderInfoList findAllOrders() {
        return new OrderInfoList(
                newArrayList(orderRepository.findAll())
                        .stream()
                        .map(order ->
                                new OrderInfo(order.getId(), newArrayList(new OrderLine(new Product(order.getProductId(), order.getProductName()), order.getQuantity())))
                        )
                        .collect(toList())
        );
    }

    public ProductList findProducts(String orderId) {
        List<Product> products = orderRepository.findById(orderId)
                .map(order -> newArrayList(new Product(order.getProductId(), order.getProductName())))
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return new ProductList(products);
    }

    public String createOrder(OrderInfo orderData) {
        Order orderToBeSave = new Order();
        orderData.getOrderLines().stream()
                .forEach(orderLineData -> {
                    orderToBeSave.setProductId(orderLineData.getProduct().getProductId());
                    orderToBeSave.setProductName(orderLineData.getProduct().getProductName());
                    orderToBeSave.setQuantity(orderLineData.getQuantity());
                });
        Order order = orderRepository.save(orderToBeSave);
        return order.getId();
    }

    public void updateOrder(OrderInfo orderData) {
        if (orderData.getId() == null) throw new RuntimeException("Order ID is null");
        // update record
        Order orderToBeSave = new Order();
        orderToBeSave.setId(orderData.getId());
        orderData.getOrderLines().stream()
                .forEach(orderLineData -> {
                    orderToBeSave.setProductId(orderLineData.getProduct().getProductId());
                    orderToBeSave.setProductName(orderLineData.getProduct().getProductName());
                    orderToBeSave.setQuantity(orderLineData.getQuantity());
                });
        orderRepository.save(orderToBeSave);
    }
}
