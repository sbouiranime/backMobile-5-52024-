package com.mobile.Projet.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.Duration;
import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Table(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="product_id")
    private long productId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @Column(name="price")
    private long price;

    @Column(name="date_post")
    private LocalDate dateOfPost;

    @Column(name="state")
    private StateEnum state;

    @Column(name="descrption")
    private String description;

    @Column(name="product_picture")
    private String productPicture;

    @Column(name="is_reserved")
    private boolean isReserved;


}
