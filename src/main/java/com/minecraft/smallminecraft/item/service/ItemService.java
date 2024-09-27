package com.minecraft.smallminecraft.item.service;

import com.minecraft.smallminecraft.item.entity.Item;
import com.minecraft.smallminecraft.item.repository.ItemRepository;
import com.minecraft.smallminecraft.member.entity.Member;
import com.minecraft.smallminecraft.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public ItemService(ItemRepository itemRepository, MemberRepository memberRepository) {
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
    }

    public ResponseEntity<Object> showList(String username) {
        List<Item> items = itemRepository.findAll();

        Member member = memberRepository.findByUsername(username);
        if (member != null) {
            // 사용자가 이미 구매한 아이템 ID 목록을 가져옴
            List<Integer> purchasedItemIds = member.getUserItems().stream()
                    .map(userItem -> userItem.getItem().getId())
                    .toList();

            List<Item> availableItems = items.stream()
                    .filter(item -> !purchasedItemIds.contains(item.getId()))
                    .toList();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(availableItems);

        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(items);
    }
}
