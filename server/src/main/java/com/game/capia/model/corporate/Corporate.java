package com.game.capia.model.corporate;

import com.game.capia.model.tech.Tech;
import com.game.capia.model.world.World;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "corporate_tb")
@Entity
public class Corporate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="World_id")
    private World world;

    @Column(name = "name")
    private String name;

    @Column(name = "logo")
    private String logo;

    private Long cash;

    @Column(name = "landAsset")
    private Long landAsset;

    @Column(name = "techAsset")
    private Long techAsset;

    @Column(name = "stockAsset")
    private Long stockAsset;

    private Long loans;
    private Long commonStock;
    private Long retainedEarnings;
    private Long dividendsPaid;
    private Long netWorth;
    private Long donation;
    private Long stockReturn;
    private Long receivedDividends;
    private Long loanInterest;
    private Long otherProfit;
    private Long netProfit;
}
