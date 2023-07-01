package com.game.capia.model.stock;

import com.game.capia.model.corporate.Corporate;

import javax.persistence.*;

@Entity
@Table(name = "stock_tb")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long price;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "issuerId")
    private Corporate issuer;

    private Long ownerId;

    // getters and setters
}
