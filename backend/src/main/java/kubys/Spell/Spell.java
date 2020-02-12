package kubys.Spell;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Spell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    String name;
    SpellType type;
    int pa;
    int minScope;
    int maxScope;
    int damage;
    int ammunition;// -1 if infinity
    int zone; // TODO make it enum
}
