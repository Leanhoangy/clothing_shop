package com.javgr.clothingshop.repository;

import com.javgr.clothingshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUserIdOrderByIdDesc(Integer userId);

    Optional<Order> findByIdAndUserId(Integer id, Integer userId);
}
