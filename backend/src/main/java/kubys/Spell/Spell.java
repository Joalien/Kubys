package kubys.Spell;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.LinkedHashMap;

@Data
@Builder
public class Spell {

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

    static LinkedHashMap<Long, Spell> spells;

    static {
        spells = new LinkedHashMap<>();
        spells.put(1L, Spell.builder()
                .type(SpellType.CLASSIC)
                .name("Tir simple")
                .ammunition(-1)
                .minScope(1)
                .maxScope(12)
                .zone(1)
                .build());

        spells.put(2L, Spell.builder()
                .type(SpellType.DROP)
                .name("Lacher simple")
                .ammunition(-1)
                .minScope(1)
                .maxScope(3)
                .zone(1)
                .build());

        spells.put(3L, Spell.builder()
                .type(SpellType.THROW)
                .name("Tir en cloche simple")
                .ammunition(-1)
                .minScope(1)
                .maxScope(10)
                .zone(1)
                .build());
    }

    public static LinkedHashMap<Long, Spell> getSpells(){
        return spells;
    }
}
