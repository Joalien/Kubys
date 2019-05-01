package kubys.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kubys.model.common.Breed;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@JsonIgnoreProperties(value = {"currentMap"})
public class Player extends Cell{

    @ToString.Exclude
    private Map currentMap;
    private Breed breed;
    private String name;
    private int level;
    //private Weapon [] weapons;
    //private Weapon currentWeapon;
    //private Spell [] spells;
    private int pm;
    private int pa;

}