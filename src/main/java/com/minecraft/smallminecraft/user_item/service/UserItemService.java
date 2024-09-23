package com.minecraft.smallminecraft.user_item.service;

import com.minecraft.smallminecraft.item.entity.Item;
import com.minecraft.smallminecraft.item.repository.ItemRepository;
import com.minecraft.smallminecraft.member.entity.Member;
import com.minecraft.smallminecraft.member.repository.MemberRepository;
import com.minecraft.smallminecraft.response.ErrorResponse;
import com.minecraft.smallminecraft.user_item.dto.ItemDTO;
import com.minecraft.smallminecraft.user_item.dto.SelectSkinRequestDTO;
import com.minecraft.smallminecraft.user_item.dto.UserItemListResponse;
import com.minecraft.smallminecraft.user_item.entity.UserItem;
import com.minecraft.smallminecraft.user_item.repository.UserItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserItemService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;

    public UserItemService(MemberRepository memberRepository, ItemRepository itemRepository, UserItemRepository userItemRepository) {
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
        this.userItemRepository = userItemRepository;
    }

    @Transactional
    public ResponseEntity<Object> getMySkins(String username) {
        Member member = memberRepository.findByUsername(username);
        List<UserItem> userItems = member.getUserItems();
        List<ItemDTO> items = new ArrayList<>();

        for (UserItem userItem : userItems) {
            Item item = userItem.getItem();
            items.add(new ItemDTO(item.getId(), item.getName(), item.getImg()));
        }

        Item selectedItem = member.getItem();
        Integer selectedSkinId = (selectedItem != null) ? selectedItem.getId() : null;

        UserItemListResponse dto = new UserItemListResponse(items, selectedSkinId);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Transactional
    public ResponseEntity<Object> selectSkin(SelectSkinRequestDTO selectSkinRequestDTO, String username) {
        Member member = memberRepository.findByUsername(username);

        Item item = itemRepository.findById(selectSkinRequestDTO.getSkinId()).orElse(null);

        if(item == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("존재하지 않는 스킨입니다."));
        }
        member.setItem(item);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
