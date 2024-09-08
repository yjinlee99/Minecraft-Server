package com.minecraft.smallminecraft.order.service;

import com.minecraft.smallminecraft.member.entity.Member;
import com.minecraft.smallminecraft.member.repository.MemberRepository;
import com.minecraft.smallminecraft.order.dtos.OrderRequest;
import com.minecraft.smallminecraft.order.dtos.OrderResponse;
import com.minecraft.smallminecraft.order.dtos.OrderVerifyRequest;
import com.minecraft.smallminecraft.order.entity.Orders;
import com.minecraft.smallminecraft.order.repository.OrderRepository;
import com.minecraft.smallminecraft.response.ErrorResponse;
import com.minecraft.smallminecraft.store.entity.Item;
import com.minecraft.smallminecraft.store.repository.ItemRepository;
import jakarta.transaction.Transactional;
import kr.co.bootpay.Bootpay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public ResponseEntity<Object> createOrder(OrderRequest orderRequest, String username) {
        Item item = itemRepository.findById(orderRequest.getItemId()).orElse(null);
        if(item == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("아이템이 존재하지 않습니다."));
        }

        Member member = memberRepository.findByUsername(username);

        Orders order = new Orders();

        order.setItem(item);
        order.setMember(member);
        order.setPrice(item.getPrice());
        order.setStatus("취소");

        Orders savedOrder = orderRepository.save(order);

        OrderResponse response = new OrderResponse(savedOrder.getId(), member);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }


    @Transactional
    public ResponseEntity<Object> verifyOrder(OrderVerifyRequest request) {
        try {
            Bootpay bootpay = new Bootpay("66d43bf5cc5274a3ac3fc172", "19mT78DxgT4aeNVZxxUTYjVjzttWHPOfJZLMukq3Iak=");
            HashMap token = bootpay.getAccessToken();
            if(token.get("error_code") != null) { //failed
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("토큰을 발급할 수 없습니다."));
            }
            String receiptId = request.getReceipt_id();
            HashMap res = bootpay.getReceipt(receiptId);
            if(res.get("error_code") == null) { //success
                System.out.println("confirm success: " + res);
            } else {
                System.out.println("confirm false: " + res);
            }

            Orders order = orderRepository.findById((Long)res.get("order_id")).orElse(null);

            if(order == null) {
                log.info("주문이 존재하지 않습니다.");
            }

            order.setStatus("결제완료");

            return ResponseEntity.status(HttpStatus.OK)
                    .build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("통신오류"));
        }
    }
}
