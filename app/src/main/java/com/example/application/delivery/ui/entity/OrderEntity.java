package com.example.application.delivery.ui.entity;


public class OrderEntity {
    int orderId;
    String foodItems;
    int quantity;
    Double orderTotal;
    CustomerEntity customerEntity;

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    int orderStatus;

    @Override
    public String toString() {
        return "OrderEntity{" +
                "orderId=" + orderId +
                ", foodItems='" + foodItems + '\'' +
                ", quantity=" + quantity +
                ", orderTotal=" + orderTotal +
                ", customerEntity=" + customerEntity +
                ", orderStatus=" + orderStatus +
                '}';
    }

    public OrderEntity(int orderId, String foodItems, int quantity, Double orderTotal, CustomerEntity customerEntity, int orderStatus) {
        this.orderId = orderId;
        this.foodItems = foodItems;
        this.quantity = quantity;
        this.orderTotal = orderTotal;
        this.customerEntity = customerEntity;
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

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }
}
