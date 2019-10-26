package kubys.Spell;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SpellType {

    CLASSIC("Classique"),
    DROP("Laché"),
    THROW("En cloche");

    private String frenchName;

    SpellType(String frenchName) {
        this.frenchName = frenchName;
    }

    @JsonValue
    public String getFrenchName() {
        return frenchName;
    }
}