package com.mobile.Projet.model;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@CrossOrigin
@NoArgsConstructor
@Getter
@Setter
@Table(name="user" ,uniqueConstraints = { @UniqueConstraint(columnNames = { "userName", "email" }) })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private long userId;

    @NotNull
    @Column(name="user_name")
    private String userName;


    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name ="phone_number")
    private Long phoneNumber;

    @NotNull
    @Column(name="password")
    private String password;


    @Column(name="user_picture")
    private String userPicture;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    @Column(name ="liked_products")
//    private List<Product> likedProducts;

    @Column(name = "user_city")
    private String userCity;

    @Column(name = "user_delegation")
    private String userDelegation;

    @Column(name = "user_address")
    private String userStreet;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Column(name = "user_orders")
    private List<Reservation> userOrders;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_product",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "user_products")
    private List<Product> userProducts;



}
