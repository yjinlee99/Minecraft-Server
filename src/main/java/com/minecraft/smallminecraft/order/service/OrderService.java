package com.minecraft.smallminecraft.order.service;

import com.minecraft.smallminecraft.member.entity.Member;
import com.minecraft.smallminecraft.member.repository.MemberRepository;
import com.minecraft.smallminecraft.order.dtos.OrderApproveRequest;
import com.minecraft.smallminecraft.order.dtos.OrderRequest;
import com.minecraft.smallminecraft.order.dtos.OrderResponse;
import com.minecraft.smallminecraft.order.dtos.OrderVerifyRequest;
import com.minecraft.smallminecraft.order.entity.Orders;
import com.minecraft.smallminecraft.order.repository.OrderRepository;
import com.minecraft.smallminecraft.response.ErrorResponse;
import com.minecraft.smallminecraft.item.entity.Item;
import com.minecraft.smallminecraft.item.repository.ItemRepository;
import com.minecraft.smallminecraft.user_item.entity.UserItem;
import com.minecraft.smallminecraft.user_item.repository.UserItemRepository;
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
    private final UserItemRepository userItemRepository;

    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, ItemRepository itemRepository, UserItemRepository userItemRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
        this.userItemRepository = userItemRepository;
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
            Bootpay bootpay = new Bootpay("66d43bf5cc5274a3ac3fc172", "j5dSuJPHGa//ScsqKw302qbic7W5ZMVTg2mmOon8izg=");
            HashMap token = bootpay.getAccessToken();
            if(token.get("error_code") != null) { //failed
                log.info((String) token.get("error_code"));
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

            // `order_id`를 String으로 먼저 가져옴
            String orderIdString = (String) res.get("order_id");

            // String을 Long으로 변환
            Long orderId = Long.parseLong(orderIdString);

            Orders order = orderRepository.findById(orderId).orElse(null);

            if(order == null) {
                log.info("주문이 존재하지 않습니다.");
            }

            order.setStatus("결제완료");
            order.setReceipt_id(request.getReceipt_id());

            return ResponseEntity.status(HttpStatus.OK)
                    .build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("서버 오류: "+e));
        }
    }

    @Transactional
    public ResponseEntity<Object> approveOrder(OrderApproveRequest request, String username) {
        try {
            // 부트페이 토큰 발급
            Bootpay bootpay = new Bootpay("66d43bf5cc5274a3ac3fc172", "j5dSuJPHGa//ScsqKw302qbic7W5ZMVTg2mmOon8izg=");
            HashMap token = bootpay.getAccessToken();
            if(token.get("error_code") != null) { //failed
                log.info((String) token.get("error_code"));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("토큰을 발급할 수 없습니다."));
            }

            // 사용자와 아이템 존재 확인
            Member member = memberRepository.findByUsername(username);
            Item item = itemRepository.findById(request.getItem_id()).orElse(null);

            if(member==null || item ==null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("사용자 혹은 아이템이 확인되지 않습니다."));
            }

            // 승인
            String receiptId = request.getReceipt_id();
            HashMap res = bootpay.confirm(receiptId);

            // 사용자 아이템으로 저장

            UserItem userItem = new UserItem();
            userItem.setMember(member);
            userItem.setItem(item);

            userItemRepository.save(userItem);

            if(res.get("error_code") == null) { //success
                System.out.println("confirm success: " + res);
                return ResponseEntity.status(HttpStatus.OK)
                        .build();
            } else {
                System.out.println("confirm false: " + res);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("승인 중 오류 발생"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("승인 중 오류 발생"));
        }
    }
}
