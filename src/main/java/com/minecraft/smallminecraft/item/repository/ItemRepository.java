package com.minecraft.smallminecraft.item.repository;

import com.minecraft.smallminecraft.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    public List<Item> findAll();
}
