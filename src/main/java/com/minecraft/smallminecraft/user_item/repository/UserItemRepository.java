package com.minecraft.smallminecraft.user_item.repository;

import com.minecraft.smallminecraft.user_item.entity.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserItemRepository extends JpaRepository<UserItem, Integer> {
}
