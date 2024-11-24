package ca.gbc.orderservice.service;

import ca.gbc.orderservice.client.InventoryClient;
import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.model.order;
import ca.gbc.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final InventoryClient inventoryClient;

    @Override
    public void placeOrder(OrderRequest orderRequest) {

        //check inventory
        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (isProductInStock) {
            order Order = order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .price(orderRequest.price())
                    .skuCode(orderRequest.skuCode())
                    .quantity(orderRequest.quantity())
                    .build();


            orderRepository.save(Order);
        }else {
            throw new RuntimeException("product with skuCode"+ orderRequest.skuCode()+ "is not in stock");
        }

    }
}
