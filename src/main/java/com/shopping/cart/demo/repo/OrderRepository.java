package com.shopping.cart.demo.repo;

import com.shopping.cart.demo.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, String> {
}
