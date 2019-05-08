package kubys.model;

import kubys.model.common.SpellType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Spell {

    String name;
    SpellType spellType;
    int pa;
    int minScope;
    int maxScope;
    int damage;
    int ammunition;// -1 if infinity
    int zone;



}
