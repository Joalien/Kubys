package kubys.model.common;


import com.fasterxml.jackson.annotation.JsonProperty;

public enum Direction {

    @JsonProperty("z")
    FORWARD('z'),
    @JsonProperty("s")
    BACKWARD('s'),
    @JsonProperty("q")
    LEFT('q'),
    @JsonProperty("d")
    RIGHT('d');


    Direction(char c) {
    }
}