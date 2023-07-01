package com.game.capia.model.character;

import com.game.capia.model.corporate.Corporate;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "character_tb")
@Entity
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "corporate_id", nullable = true)
    private Corporate corporate;

    @Column(nullable = false)
    private String fullname;

    private CharacterGender gender;

    @Column(nullable = false)
    private CharController charController;

    private JobTitle jobTitle;

    //텍스쳐 개수는 CharacterBase 에 저장하고. 텍스쳐중에서 랜덤선택
    @Column(nullable = false)
    private String texture;

    //능력치에 따라 산정되게 해야할듯
    @Column(nullable = false)
    private Long annualSalary;

    //능력치 랜덤생성해야할듯
    //각 능력치가 어떤 작용을 할지도 결정해야한다
    @Column(nullable = false)
    private Integer businessExpertise;
    @Column(nullable = false)
    private Integer retailing;
    @Column(nullable = false)
    private Integer farming;
    @Column(nullable = false)
    private Integer manufacturing;
    @Column(nullable = false)
    private Integer researchNdevelopment;
    @Column(nullable = false)
    private Integer rawMaterialProduction;
    @Column(nullable = false)
    private Integer marketing;
    @Column(nullable = false)
    private Integer training;
    @Column(nullable = false)
    private Integer electorics;

}
