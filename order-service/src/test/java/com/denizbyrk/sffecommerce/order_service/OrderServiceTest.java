package com.denizbyrk.sffecommerce.order_service;

import com.denizbyrk.sffecommerce.order_service.entity.Order;
import com.denizbyrk.sffecommerce.order_service.client.UserClient;
import com.denizbyrk.sffecommerce.order_service.entity.OrderStatus;
import com.denizbyrk.sffecommerce.order_service.DTO.UserResponseDTO;
import com.denizbyrk.sffecommerce.order_service.entity.PaymentStatus;
import com.denizbyrk.sffecommerce.order_service.DTO.OrderResponseDTO;
import com.denizbyrk.sffecommerce.order_service.client.ProductClient;
import com.denizbyrk.sffecommerce.order_service.service.OrderService;
import com.denizbyrk.sffecommerce.order_service.DTO.ProductResponseDTO;
import com.denizbyrk.sffecommerce.order_service.DTO.OrderItemRequestDTO;
import com.denizbyrk.sffecommerce.order_service.DTO.CreateOrderRequestDTO;
import com.denizbyrk.sffecommerce.order_service.repository.OrderRepository;
import com.denizbyrk.sffecommerce.order_service.producer.NotificationProducer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.*;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductClient productClient;

    @Mock
    private UserClient userClient;

    @Mock
    private NotificationProducer notificationProducer;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setupSecurity() {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "deniz",
                        null
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }

    @Test
    void createOrder_shouldCreateOrderSuccessfully() {

        UserResponseDTO user = UserResponseDTO.builder()
                .id(1L)
                .username("deniz")
                .email("denizb@xxx.com")
                .build();

        ProductResponseDTO product =
                ProductResponseDTO.builder()
                        .id(10L)
                        .name("Laptop")
                        .price(1000.0)
                        .sellerId(5L)
                        .build();

        when(this.userClient.getUserByUsername("deniz")).thenReturn(user);

        when(this.productClient.getProductById(10L)).thenReturn(product);

        when(this.userClient.getUserById(1L)).thenReturn(user);

        Order savedOrder =
                Order.builder()
                        .id(1L)
                        .userId(1L)
                        .totalPrice(2000.0)
                        .orderStatus(OrderStatus.PENDING)
                        .paymentStatus(PaymentStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .items(new ArrayList<>())
                        .build();

        when(this.orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderItemRequestDTO item =
                new OrderItemRequestDTO();

        item.setProductId(10L);
        item.setQuantity(2);

        CreateOrderRequestDTO request = new CreateOrderRequestDTO();

        request.setItems(List.of(item));

        OrderResponseDTO response = this.orderService.createOrder(request);

        assertNotNull(response);

        assertEquals(1L, response.getOrderId());

        assertEquals(
                2000.0,
                response.getTotalPrice()
        );

        verify(this.orderRepository).save(any(Order.class));

        verify(this.notificationProducer).sendOrderCreatedEvent(any());
    }

    @Test
    void getMyOrders_shouldReturnUserOrders() {

        UserResponseDTO user =
                UserResponseDTO.builder()
                        .id(1L)
                        .username("deniz")
                        .build();

        when(this.userClient.getUserByUsername("deniz")).thenReturn(user);

        Order order =
                Order.builder()
                        .id(1L)
                        .userId(1L)
                        .totalPrice(500.0)
                        .orderStatus(OrderStatus.PENDING)
                        .paymentStatus(PaymentStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .items(new ArrayList<>())
                        .build();

        when(this.orderRepository.findByUserId(1L)).thenReturn(List.of(order));

        List<OrderResponseDTO> response = orderService.getMyOrders();

        assertEquals(1, response.size());

        assertEquals(
                500.0,
                response.get(0).getTotalPrice()
        );
    }

    @Test
    void getOrderById_shouldReturnOrder_whenOwner() {

        UserResponseDTO user =
                UserResponseDTO.builder()
                        .id(1L)
                        .username("deniz")
                        .build();

        when(this.userClient.getUserByUsername("deniz")).thenReturn(user);

        Order order =
                Order.builder()
                        .id(1L)
                        .userId(1L)
                        .totalPrice(300.0)
                        .orderStatus(OrderStatus.PENDING)
                        .paymentStatus(PaymentStatus.PENDING)
                        .items(new ArrayList<>())
                        .build();

        when(this.orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponseDTO response = this.orderService.getOrderById(1L);

        assertEquals(
                1L,
                response.getOrderId()
        );
    }

    @Test
    void getOrderById_shouldThrowException_whenNotOwner() {

        UserResponseDTO user =
                UserResponseDTO.builder()
                        .id(2L)
                        .username("deniz")
                        .build();

        when(this.userClient.getUserByUsername("deniz")).thenReturn(user);

        Order order =
                Order.builder()
                        .id(1L)
                        .userId(1L)
                        .items(new ArrayList<>())
                        .build();

        when(this.orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(
                RuntimeException.class,
                () -> this.orderService.getOrderById(1L)
        );
    }

    @Test
    void cancelOrder_shouldCancelPendingOrder() {

        UserResponseDTO user =
                UserResponseDTO.builder()
                        .id(1L)
                        .username("deniz")
                        .build();

        when(this.userClient.getUserByUsername("deniz"))
                .thenReturn(user);

        Order order =
                Order.builder()
                        .id(1L)
                        .userId(1L)
                        .orderStatus(OrderStatus.PENDING)
                        .items(new ArrayList<>())
                        .build();

        when(this.orderRepository.findById(1L)).thenReturn(Optional.of(order));

        this.orderService.cancelOrder(1L);

        assertEquals(
                OrderStatus.CANCELLED,
                order.getOrderStatus()
        );

        verify(this.orderRepository).save(order);
    }

    @Test
    void markOrderAsPaid_shouldUpdatePaymentStatus() {

        Order order =
                Order.builder()
                        .id(1L)
                        .paymentStatus(PaymentStatus.PENDING)
                        .items(new ArrayList<>())
                        .build();

        when(this.orderRepository.findById(1L)).thenReturn(Optional.of(order));

        this.orderService.markOrderAsPaid(1L);

        assertEquals(
                PaymentStatus.PAID,
                order.getPaymentStatus()
        );

        verify(this.orderRepository).save(order);
    }

    @Test
    void updateStatus_shouldUpdateOrderStatus() {

        Order order =
                Order.builder()
                        .id(1L)
                        .orderStatus(OrderStatus.PENDING)
                        .items(new ArrayList<>())
                        .build();

        when(this.orderRepository.findById(1L)).thenReturn(Optional.of(order));

        this.orderService.updateStatus(
                1L,
                OrderStatus.SHIPPED
        );

        assertEquals(
                OrderStatus.SHIPPED,
                order.getOrderStatus()
        );
    }
}