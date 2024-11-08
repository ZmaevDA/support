package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "resist")
public class Resist {

    public Resist(Integer vitality, Integer focus, Integer robustness, Integer immunity, Integer holy, Integer lightning, Integer fire, Integer magic, Integer pierce, Integer splash, Integer strike, Integer physical) {
        this.vitality = vitality;
        this.focus = focus;
        this.robustness = robustness;
        this.immunity = immunity;
        this.holy = holy;
        this.lightning = lightning;
        this.fire = fire;
        this.magic = magic;
        this.pierce = pierce;
        this.splash = splash;
        this.strike = strike;
        this.physical = physical;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resist_id_seq")
    @SequenceGenerator(name = "resist_id_seq", sequenceName = "resist_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "vitality")
    private Integer vitality;

    @Column(name = "focus")
    private Integer focus;

    @Column(name = "robustness")
    private Integer robustness;

    @Column(name = "immunity")
    private Integer immunity;

    @Column(name = "holy")
    private Integer holy;

    @Column(name = "lightning")
    private Integer lightning;

    @Column(name = "fire")
    private Integer fire;

    @Column(name = "magic")
    private Integer magic;

    @Column(name = "pierce")
    private Integer pierce;

    @Column(name = "splash")
    private Integer splash;

    @Column(name = "strike")
    private Integer strike;

    @Column(name = "physical")
    private Integer physical;

    @OneToOne(mappedBy = "resist", fetch = FetchType.EAGER)
    private InventoryItem item;
}
