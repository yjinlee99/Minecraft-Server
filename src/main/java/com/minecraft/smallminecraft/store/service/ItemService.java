package com.minecraft.smallminecraft.store.service;

import com.minecraft.smallminecraft.store.entity.Item;
import com.minecraft.smallminecraft.store.repository.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ResponseEntity<Object> showList() {
        List<Item> items = itemRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(items);
    }
}
