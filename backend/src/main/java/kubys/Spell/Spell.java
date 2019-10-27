package kubys.Spell;

import kubys.Player.Breed;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.LinkedHashMap;

@Data
@Builder
public class Spell {

    String name;
    SpellType type;
    int pa;
    int minScope;
    int maxScope;
    int damage;
    int ammunition;// -1 if infinity
    int zone;
    Breed breed;

    static LinkedHashMap<Long, Spell> spells;

    static {
        spells = new LinkedHashMap<>();
        spells.put(1L, Spell.builder()
                .type(SpellType.CLASSIC)
                .name("Tir simple")
                .ammunition(2)
                .minScope(1)
                .maxScope(12)
                .zone(1)
                .breed(Breed.DWARF)
                .build());

        spells.put(2L, Spell.builder()
                .type(SpellType.DROP)
                .name("Lacher simple")
                .ammunition(-1)
                .minScope(1)
                .maxScope(3)
                .zone(1)
                .breed(Breed.DWARF)
                .build());

        spells.put(3L, Spell.builder()
                .type(SpellType.THROW)
                .name("Tir en cloche simple")
                .ammunition(-1)
                .minScope(1)
                .maxScope(10)
                .zone(1)
                .breed(Breed.DWARF)
                .build());
        for (long i = 4L; i<=17; i++) {
            spells.put(i, Spell.builder()
                    .type(SpellType.THROW)
                    .name("Hachachinage " + i)
                    .ammunition(-1)
                    .minScope((int) (i % 3) + 1)
                    .maxScope((int) (i % 3) + 1)
                    .zone((int) ((i + 1) % 2) + 1)
                    .breed(Breed.DWARF)
                    .build());
        }

    }

    public static LinkedHashMap<Long, Spell> getSpells(){
        return spells;
    }
}
