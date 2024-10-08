package com.bookstore.project.service.impl;


import com.bookstore.project.entity.Book;
import com.bookstore.project.entity.Order;
import com.bookstore.project.entity.OrderItem;
import com.bookstore.project.entity.User;
import com.bookstore.project.exception.RestException;
import com.bookstore.project.repository.OrderRepository;
import com.bookstore.project.repository.UserRepository;
import com.bookstore.project.request.OrderItemRequest;
import com.bookstore.project.request.OrderRequest;
import com.bookstore.project.responses.OrderItemResponse;
import com.bookstore.project.responses.OrderResponse;
import com.bookstore.project.repository.BookRepository;
import com.bookstore.project.repository.OrderItemRepository;
import com.bookstore.project.responses.RevenueByCategoryResponse;
import com.bookstore.project.responses.RevenueResponse;
import com.bookstore.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // Create new Order entity
        Order order = new Order();

        // Find the user (customer) based on the request's userId and set the user
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + orderRequest.getUserId()));

        order.setUser(user);  // Assign user to order

        // Set the order date
        order.setOrderDate(new Date());

        BigDecimal totalPrice = BigDecimal.ZERO;

        // Process each order item from the request
        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            Book book = bookRepository.findById(itemRequest.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found with id: " + itemRequest.getBookId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(itemRequest.getQuantity());

            BigDecimal itemPrice = book.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            orderItem.setPrice(itemPrice);

            totalPrice = totalPrice.add(itemPrice);

            order.getOrderItems().add(orderItem);
        }

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        // Prepare the OrderResponse with formatted date
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getId());

        // Convert the Date or Timestamp to LocalDateTime and format it
        LocalDateTime localDateTime = order.getOrderDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
        String formattedOrderDate = localDateTime.format(formatter);

        // Set the formatted order date
        orderResponse.setOrderDate(formattedOrderDate);

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

    @Transactional
    public OrderResponse editOrder(Long orderId, OrderRequest orderRequest) {
        // Find the existing order by its ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        // Find the user (customer) based on the request's userId and set the user (if updatable)
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + orderRequest.getUserId()));

        order.setUser(user); // Update the user if necessary

        // Update the order date (if required)
        order.setOrderDate(new Date());

        BigDecimal totalPrice = BigDecimal.ZERO;

        // Clear existing order items and update them with the new ones from the request
        order.getOrderItems().clear();

        // Process each order item from the request
        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            Book book = bookRepository.findById(itemRequest.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found with id: " + itemRequest.getBookId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(itemRequest.getQuantity());

            BigDecimal itemPrice = book.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            orderItem.setPrice(itemPrice);

            totalPrice = totalPrice.add(itemPrice);

            order.getOrderItems().add(orderItem);
        }

        // Update the total price
        order.setTotalPrice(totalPrice);

        // Save the updated order
        orderRepository.save(order);

        // Prepare the updated OrderResponse with formatted date
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getId());

        // Convert the Date or Timestamp to LocalDateTime and format it
        LocalDateTime localDateTime = order.getOrderDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
        String formattedOrderDate = localDateTime.format(formatter);

        // Set the formatted order date
        orderResponse.setOrderDate(formattedOrderDate);

        // Set the total price
        orderResponse.setTotalPrice(order.getTotalPrice());

        // Map the updated order items to OrderItemResponse
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream().map(item -> {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setBookId(item.getBook().getId());
            itemResponse.setBookTitle(item.getBook().getTitle());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPrice(item.getPrice());
            return itemResponse;
        }).collect(Collectors.toList());

        // Set the order items in the response
        orderResponse.setOrderItems(itemResponses);

        return orderResponse;
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        // Find the order by ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> RestException.notFound("Order not found"));

        // Delete the order
        orderRepository.delete(order);
    }

    public List<OrderResponse> getOwnOrders() {
        // Extract the currently authenticated user's email (assuming JWT authentication)
        String email = getCurrentUserEmail();

        // Fetch the user entity using the email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Fetch orders belonging to the user
        List<Order> orders = orderRepository.findByUser(user);

        // Map the orders to OrderResponse and return
        return orders.stream().map(this::mapToOrderResponse).collect(Collectors.toList());
    }

    // Helper method to get the current user's email from the SecurityContext
    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // This is typically the email in your case
        } else {
            return principal.toString();
        }
    }

    @Transactional(readOnly = true)
    public List<RevenueResponse> calculateDailyRevenue() {
        List<Object[]> result = orderRepository.calculateTotalRevenueByDay();

        return result.stream().map(row -> {
            Date date = (Date) row[0];  // The date (grouped by)
            BigDecimal totalRevenue = (BigDecimal) row[1];  // The total revenue for that day

            RevenueResponse revenueResponse = new RevenueResponse();
            revenueResponse.setDate(date);
            revenueResponse.setTotalRevenue(totalRevenue);

            return revenueResponse;
        }).collect(Collectors.toList());
    }


    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getId());

        // Convert the Date to LocalDateTime and format it
        LocalDateTime localDateTime = order.getOrderDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
        String formattedOrderDate = localDateTime.format(formatter);

        // Set the formatted order date
        orderResponse.setOrderDate(formattedOrderDate);

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

    @Transactional(readOnly = true)
    public List<RevenueByCategoryResponse> calculateRevenueByCategory() {
        List<Object[]> result = orderRepository.calculateTotalRevenueByCategory();

        return result.stream().map(row -> {
            String category = (String) row[0];  // The category (grouped by)
            BigDecimal totalRevenue = (BigDecimal) row[1];  // The total revenue for that category

            RevenueByCategoryResponse revenueResponse = new RevenueByCategoryResponse();
            revenueResponse.setCategory(category);
            revenueResponse.setTotalRevenue(totalRevenue);

            return revenueResponse;
        }).collect(Collectors.toList());
    }

}

