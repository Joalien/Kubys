package kubys.model;

import kubys.model.common.Breed;
import kubys.model.common.Position;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@Entity
public class Player extends Cell{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;


    @Embedded
    private Position position;

    @Column
    @Enumerated(EnumType.STRING)
    private Breed breed;
    @Column
    private String name;
    @Column
    private int level;
    //private Weapon [] weapons;
    //private Weapon currentWeapon;
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Spell> spells = new LinkedHashSet<>();
    @Column
    private int pm;
    @Column
    private int pa;
}