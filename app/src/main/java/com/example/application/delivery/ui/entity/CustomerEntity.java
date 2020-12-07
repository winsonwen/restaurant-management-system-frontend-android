package com.example.application.delivery.ui.entity;

public class CustomerEntity {

    private String firstName;
    private String lastName;
    private String phone;
    public String stressName;

    @Override
    public String toString() {
        return "CustomerEntity{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", stressName='" + stressName + '\'' +
                '}';
    }

    public CustomerEntity(String firstName, String lastName, String phone, String stressName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.stressName = stressName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getStressName() {
        return stressName;
    }

    public void setStressName(String stressName) {
        this.stressName = stressName;
    }
}
