package com.company.pojo;

import com.company.annotation.Value;

public class Address {
    @Value("uway")
    String street;
    @Value("seattle")
    String city;

    public Address() {
    }

    public Address(String street, String city) {
        this.street = street;
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
