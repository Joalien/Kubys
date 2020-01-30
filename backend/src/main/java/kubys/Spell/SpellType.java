package kubys.Spell;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SpellType {

    CLASSIC("Classique"),
    DROP("Laché"),
    THROW("En cloche");

    private String name;

    SpellType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}