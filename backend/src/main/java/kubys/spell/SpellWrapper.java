package kubys.spell;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class SpellWrapper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Spell spell;
    @Column
    private boolean unlock;


    SpellWrapper(Spell element) {
        this.spell = element;
        this.unlock = false;
    }
}
