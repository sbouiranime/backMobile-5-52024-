package com.mobile.Projet.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reservationId")
    private long reservationId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @Column(name="reservation_max_duration")
    private Duration reservationMAxDuration;

    @Column(name = "date_reservation")
    private LocalDate dateOfReservation;


}
