package com.game.capia.model.firm;

import javax.persistence.*;

@Entity
@Table(name = "firmbase_tb")
public class FirmBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer width;

    private Integer height;

    private String type;

    private Integer employee;

    private Long businessAsset;

    private Long operatingOverhead;

    private Long writeOffs;

    // getters and setters
}
