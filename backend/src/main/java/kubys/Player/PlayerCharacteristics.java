package kubys.Player;

import kubys.Spell.SpellWrapper;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PlayerCharacteristics {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;
    @Column
    protected String name;
    @Column
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    protected List<SpellWrapper> spells;
}
