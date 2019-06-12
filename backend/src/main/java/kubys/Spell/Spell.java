package kubys.Spell;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
@Entity
public class Spell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    String name;
    @Enumerated(EnumType.STRING)
    SpellType type;
    @Column
    int pa;
    @Column
    int minScope;
    @Column
    int maxScope;
    @Column
    int damage;
    @Column
    int ammunition;// -1 if infinity
    @Column
    int zone;
    @Column
    int level;



}
