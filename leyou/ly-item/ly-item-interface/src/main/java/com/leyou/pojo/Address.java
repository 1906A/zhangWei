package com.leyou.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "tb_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String state;
    private String city;
    private String district;
    private String address;
    @Column(name = "zipCode")
    private String zipcode;
    private Boolean defaulte;
    @Transient
    private String all;
}
