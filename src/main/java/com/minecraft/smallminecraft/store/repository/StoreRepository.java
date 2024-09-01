package com.minecraft.smallminecraft.store.repository;

import com.minecraft.smallminecraft.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

    public List<Store> findAll();
}
