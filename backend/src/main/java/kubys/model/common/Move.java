package kubys.model.common;


import com.fasterxml.jackson.annotation.JsonProperty;

public enum Move {

    @JsonProperty("z")
    FORWARD('z'),
    @JsonProperty("s")
    BACKWARD('s'),
    @JsonProperty("q")
    LEFT('q'),
    @JsonProperty("d")
    RIGHT('d'),
    @JsonProperty("CREATE")
    CREATE(null),
    @JsonProperty("REMOVE")
    REMOVE(null);


    Move(Character c) {
    }
}