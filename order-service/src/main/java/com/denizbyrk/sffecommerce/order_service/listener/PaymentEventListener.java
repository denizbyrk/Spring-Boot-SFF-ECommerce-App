package com.denizbyrk.sffecommerce.order_service.listener;

import com.denizbyrk.sffecommerce.order_service.config.RabbitMQConfig;
import com.denizbyrk.sffecommerce.order_service.event.PaymentCompletedEvent;
import com.denizbyrk.sffecommerce.order_service.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentEventListener {

    private final OrderService orderService;

    public PaymentEventListener(OrderService orderService) {

        this.orderService = orderService;
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {

        log.info(
                "Received PaymentCompletedEvent: paymentId={}, orderId={}",
                event.getPaymentId(),
                event.getOrderId()
        );


        this.orderService.markOrderAsPaid(event.getOrderId());
    }
}