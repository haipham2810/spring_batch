package com.example.spb_dlsource_into_db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name="DL_CUSTOMERS_INFO")
@AllArgsConstructor
@NoArgsConstructor
public class DLCustomer {
    @Id
    //Auto increament id generation for
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long ịd;
    @Column(name="uid")
    private long uid;
    @Column(name="Customer_Name")
    private String Customer_name;
    @Column(name="Extenal_Id")
    private  String External_Id;
    @Column(name="Gender")
    private String Gender;
    @Column(name="Date_of_Registration")
    private LocalDateTime Registration_Date;
}
