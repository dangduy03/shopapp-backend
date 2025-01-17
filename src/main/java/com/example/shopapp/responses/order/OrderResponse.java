package com.example.shopapp.responses.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.shopapp.models.Order;
import com.example.shopapp.models.OrderDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
	
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @JsonProperty("note")
    private String note;

    @JsonProperty("order_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime orderDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("total_money")
    private double totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod = "";

    @JsonProperty("shipping_address")
    private String shippingAddress = "";

    @JsonProperty("shipping_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate shippingDate;

    @JsonProperty("payment_method")
    private String paymentMethod = "";

    @JsonProperty("order_details")
    private List<OrderDetailResponse> orderDetails;

    public static OrderResponse fromOrder(Order order) {
        List<OrderDetail> orderDetails = order.getOrderDetails();
        
        List<OrderDetailResponse> orderDetailResponses = orderDetails
                .stream()
                .map(orderDetail -> OrderDetailResponse.fromOrderDetail(orderDetail)).toList();
        
        OrderResponse orderResponse =  OrderResponse
                .builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .fullName(order.getFullName())
                .phoneNumber(order.getPhoneNumber())
                .email(order.getEmail())
                .address(order.getAddress())
                .note(order.getNote())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalMoney(order.getTotalMoney())
                .shippingMethod(order.getShippingMethod())
                .shippingAddress(order.getShippingAddress())
                .shippingDate(order.getShippingDate())
                .paymentMethod(order.getPaymentMethod())
                .orderDetails(orderDetailResponses)
                .build();
        return orderResponse;
    }
}
