package com.javgr.clothingshop.repository;

import com.javgr.clothingshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUserIdOrderByIdDesc(Integer userId);

    Optional<Order> findByIdAndUserId(Integer id, Integer userId);

    @Query(value = "SELECT IFNULL(SUM(total_amount),0) FROM orders", nativeQuery = true)
    BigDecimal totalRevenue();

    @Query(value = "SELECT MONTH(created_at) as m, IFNULL(SUM(total_amount),0) as s FROM orders WHERE YEAR(created_at)=:yr GROUP BY MONTH(created_at) ORDER BY m", nativeQuery = true)
    List<Object[]> monthlyRevenue(@Param("yr") int year);
}
