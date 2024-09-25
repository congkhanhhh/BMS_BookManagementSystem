package com.bookstore.project.service;


import com.bookstore.project.entity.Book;
import com.bookstore.project.entity.Order;
import com.bookstore.project.entity.OrderItem;
import com.bookstore.project.entity.User;
import com.bookstore.project.repository.BookRepository;
import com.bookstore.project.repository.OrderItemRepository;
import com.bookstore.project.repository.OrderRepository;
import com.bookstore.project.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // Create new Order entity
        Order order = new Order();

        // Find the user (customer) based on the request's userId and set the user
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + orderRequest.getUserId()));

        order.setUser(user);  // Assign user to order
        order.setOrderDate(new Date());
        order.setTotalPrice(BigDecimal.ZERO);  // Initially set total price to zero

        BigDecimal totalPrice = BigDecimal.ZERO;

        // Process each order item from the request
        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            // Find book by ID from item request
            Book book = bookRepository.findById(itemRequest.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found with id: " + itemRequest.getBookId()));

            // Create new OrderItem entity and calculate price
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(book.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));

            // Add the price of this item to the total price
            totalPrice = totalPrice.add(orderItem.getPrice());

            // Add the item to the order
            order.getOrderItems().add(orderItem);
        }

        // Set the final total price for the order
        order.setTotalPrice(totalPrice);

        // Save the order in the database
        orderRepository.save(order);

        // Prepare the OrderResponse with details
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getId());
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setTotalPrice(order.getTotalPrice());

        // Map the OrderItems to the response DTO
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream().map(item -> {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setBookId(item.getBook().getId());
            itemResponse.setBookTitle(item.getBook().getTitle());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPrice(item.getPrice());
            return itemResponse;
        }).collect(Collectors.toList());

        // Set the mapped order items in the response
        orderResponse.setOrderItems(itemResponses);

        return orderResponse;
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::mapToOrderResponse).collect(Collectors.toList());
    }


    // Get order by ID
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        return mapToOrderResponse(order);
    }


    public List<OrderResponse> searchOrders(String userName) {
        List<Order> orders = orderRepository.findByUserUsernameContainingIgnoreCase(userName);
        return orders.stream().map(this::mapToOrderResponse).collect(Collectors.toList());
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getId());
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setTotalPrice(order.getTotalPrice());

        List<OrderItemResponse> itemResponses = order.getOrderItems().stream().map(item -> {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setBookId(item.getBook().getId());
            itemResponse.setBookTitle(item.getBook().getTitle());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPrice(item.getPrice());
            return itemResponse;
        }).collect(Collectors.toList());

        orderResponse.setOrderItems(itemResponses);
        return orderResponse;
    }
}

