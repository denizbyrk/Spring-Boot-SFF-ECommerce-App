package com.denizbyrk.sffecommerce.order_service.service;

import com.denizbyrk.sffecommerce.order_service.entity.Order;
import com.denizbyrk.sffecommerce.order_service.entity.OrderItem;
import com.denizbyrk.sffecommerce.order_service.client.UserClient;
import com.denizbyrk.sffecommerce.order_service.entity.OrderStatus;
import com.denizbyrk.sffecommerce.order_service.DTO.UserResponseDTO;
import com.denizbyrk.sffecommerce.order_service.client.ProductClient;
import com.denizbyrk.sffecommerce.order_service.DTO.OrderResponseDTO;
import com.denizbyrk.sffecommerce.order_service.entity.PaymentStatus;
import com.denizbyrk.sffecommerce.order_service.DTO.ProductResponseDTO;
import com.denizbyrk.sffecommerce.order_service.event.OrderCreatedEvent;
import com.denizbyrk.sffecommerce.order_service.DTO.OrderItemRequestDTO;
import com.denizbyrk.sffecommerce.order_service.DTO.OrderItemResponseDTO;
import com.denizbyrk.sffecommerce.order_service.DTO.CreateOrderRequestDTO;
import com.denizbyrk.sffecommerce.order_service.repository.OrderRepository;
import com.denizbyrk.sffecommerce.order_service.producer.NotificationProducer;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final UserClient userClient;
    private final NotificationProducer notificationProducer;

    public OrderService(OrderRepository orderRepository, ProductClient productClient, UserClient userClient, NotificationProducer notificationProducer) {

        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.userClient = userClient;
        this.notificationProducer = notificationProducer;
    }

    ///
    /// create order
    ///
    public OrderResponseDTO createOrder(CreateOrderRequestDTO request) {

        Long userId = this.getCurrentUserId();

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (OrderItemRequestDTO itemRequest : request.getItems()) {

            ProductResponseDTO product = this.productClient.getProductById(itemRequest.getProductId());
            double subtotal = product.getPrice() * itemRequest.getQuantity();

            OrderItem item = OrderItem.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .price(product.getPrice())
                    .quantity(itemRequest.getQuantity())
                    .subtotal(subtotal)
                    .build();

            orderItems.add(item);

            total += subtotal;
        }

        Order order = Order.builder()
                .userId(userId)
                .orderStatus(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .totalPrice(total)
                .items(orderItems)
                .build();

        orderItems.forEach(item -> item.setOrder(order));
        Order savedOrder = this.orderRepository.save(order);

        this.notificationProducer.sendOrderCreatedEvent(
                OrderCreatedEvent.builder()
                        .orderId(savedOrder.getId())
                        .email(this.userClient.getUserById(savedOrder.getUserId()).getEmail())
                        .totalAmount(savedOrder.getTotalPrice())
                        .build()
        );

        return this.mapToResponse(savedOrder);
    }

    ///
    /// get logged in users orders
    ///
    public List<OrderResponseDTO> getMyOrders() {

        Long userId = this.getCurrentUserId();

        return this.orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    ///
    /// get order by ID
    ///
    public OrderResponseDTO getOrderById(Long orderId) {

        Long userId = this.getCurrentUserId();

        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserId().equals(userId)) {

            throw new RuntimeException("You cannot access this order");
        }

        return this.mapToResponse(order);
    }

    public OrderResponseDTO getInternalOrderById(Long orderId) {

        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return this.mapToResponse(order);
    }

    ///
    /// cancel order
    ///
    @Transactional
    public void cancelOrder(Long orderId) {

        Long userId = this.getCurrentUserId();

        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserId().equals(userId)) {

            throw new RuntimeException("You cannot cancel this order");
        }

        if (order.getOrderStatus() != OrderStatus.PENDING) {

            throw new RuntimeException("Only pending orders can be cancelled");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);

        this.orderRepository.save(order);
    }

    ///
    /// get all orders
    ///
    public List<OrderResponseDTO> getAllOrders() {

        return this.orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void markOrderAsPaid(Long id) {

        Order order = this.orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found."));

        order.setPaymentStatus(PaymentStatus.PAID);

        this.orderRepository.save(order);
    }

    @Transactional
    public void updateStatus(Long orderId, OrderStatus status) {

        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(status);
    }

    private Long getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserResponseDTO user = this.userClient.getUserByUsername(username);
        return user.getId();
    }

    private OrderResponseDTO mapToResponse(Order order) {

        return new OrderResponseDTO(
                order.getId(),
                order.getUserId(),
                order.getTotalPrice(),
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getCreatedAt(),
                order.getItems().stream().map(this::mapItem).toList()
        );
    }

    private OrderItemResponseDTO mapItem(OrderItem item) {

        return new OrderItemResponseDTO(
                item.getProductId(),
                item.getProductName(),
                item.getPrice(),
                item.getQuantity(),
                item.getSubtotal()
        );
    }
}