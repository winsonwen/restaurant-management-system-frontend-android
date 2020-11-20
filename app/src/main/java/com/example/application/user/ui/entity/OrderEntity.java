package com.example.application.user.ui.entity;

public class OrderEntity {
    int orderId;
    String foodItems;
    int quantity;
    Double orderTotal;
    UserEntity userEntity;

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    int orderStatus;


    public OrderEntity(int orderId, String foodItems, int quantity, Double orderTotal, UserEntity userEntity, int orderStatus) {
        this.orderId = orderId;
        this.foodItems = foodItems;
        this.quantity = quantity;
        this.orderTotal = orderTotal;
        this.userEntity = userEntity;
        this.orderStatus = orderStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(String foodItems) {
        this.foodItems = foodItems;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
