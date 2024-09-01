package com.minecraft.smallminecraft.store.service;

import com.minecraft.smallminecraft.store.entity.Store;
import com.minecraft.smallminecraft.store.repository.StoreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public ResponseEntity<Object> showList() {
        List<Store> stores = storeRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(stores);
    }
}
