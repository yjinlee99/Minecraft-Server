package com.minecraft.smallminecraft.order.repository;

import com.minecraft.smallminecraft.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
