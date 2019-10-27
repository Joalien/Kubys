package kubys.Player;

import com.fasterxml.jackson.annotation.JsonBackReference;
import kubys.Map.Cell;
import kubys.Map.Position;
import kubys.Spell.Spell;
import kubys.Spell.SpellPlayer;
import kubys.User.User;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Player extends Cell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @ManyToOne(cascade=CascadeType.ALL)
    @JsonBackReference
    private User user;

    @Embedded
    private Position position;

    @Column
    @Enumerated(EnumType.STRING)
    private Breed breed;
    @Column
    private String name;
    @Column
    @NumberFormat
    private int level;
    //private Weapon [] weapons;
    //private Weapon currentWeapon;
    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "player")
    @JsonBackReference
    @Builder.Default
    private Set<SpellPlayer> spellsPlayer = new LinkedHashSet<>();
    @Column
    @NumberFormat
    private int pm;
    @Column
    @NumberFormat
    private int pa;

    public String toString() {
        return "Player(id=" + this.getId() + ", user(uid, name)=" + this.getUser().getUid() + ", " + this.getUser().getDisplayName() + ", position=" + this.getPosition() + ", breed=" + this.getBreed() + ", name=" + this.getName() + ", level=" + this.getLevel() + ", pm=" + this.getPm() + ", pa=" + this.getPa() + ")";
    }
}