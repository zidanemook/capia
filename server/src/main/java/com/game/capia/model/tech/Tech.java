package com.game.capia.model.tech;

import com.game.capia.model.corporate.Corporate;

import javax.persistence.*;

@Entity
@Table(name = "tech_tb")
public class Tech {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="Corporate_id", nullable=false)
    private Corporate corporate;

    private Long productBaseId;

    private Long ownedTech;

}
