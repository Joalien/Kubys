package kubys.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kubys.model.common.Breed;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashSet;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@JsonIgnoreProperties(value = {"currentMap"})
public class Player extends Cell{


    private int id;
//    @ToString.Exclude
//    private Map currentMap;
    private Breed breed;
    private String name;
    private int level;
    //private Weapon [] weapons;
    //private Weapon currentWeapon;
    private LinkedHashSet<Spell> spells;
    private int pm;
    private int pa;
    private boolean isConnected;

}