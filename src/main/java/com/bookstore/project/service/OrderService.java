package com.bookstore.project.service;


import com.bookstore.project.entity.Book;
import com.bookstore.project.entity.Order;
import com.bookstore.project.entity.OrderItem;
import com.bookstore.project.entity.User;
import com.bookstore.project.repository.BookRepository;
import com.bookstore.project.repository.OrderItemRepository;
import com.bookstore.project.repository.OrderRepository;
import com.bookstore.project.request.OrderItemRequest;
import com.bookstore.project.request.OrderRequest;
import com.bookstore.project.responses.OrderItemResponse;
import com.bookstore.project.responses.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // Create new Order entity
        Order order = new Order();
        order.setUser(new User());  // Assuming customer exists
        order.setOrderDate(new Date());
        order.setTotalPrice(BigDecimal.ZERO);  // Set to zero initially

        BigDecimal totalPrice = BigDecimal.ZERO;

        // Process each order item from the request
        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            Book book = bookRepository.findById(Math.toIntExact(itemRequest.getBookId()))
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            // Create new OrderItem entity
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(book.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));

            totalPrice = totalPrice.add(orderItem.getPrice());
            order.getOrderItems().add(orderItem);
        }

        // Set the total price for the order
        order.setTotalPrice(totalPrice);

        // Save the order
        orderRepository.save(order);

        // Prepare response
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getId());
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setTotalPrice(order.getTotalPrice());

        // Map each OrderItem to the response
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream().map(item -> {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setBookId((long) item.getBook().getId());
            itemResponse.setBookTitle(item.getBook().getTitle());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPrice(item.getPrice());
            return itemResponse;
        }).collect(Collectors.toList());

        orderResponse.setOrderItems(itemResponses);
        return orderResponse;
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get order by ID
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // Search orders by customer name
    public List<Order> searchOrders(String userName) {
        return orderRepository.findByUserUsernameContainingIgnoreCase(userName);
    }
}

