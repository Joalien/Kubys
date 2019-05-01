package hello.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;

@Builder
@Data
public class Map {

    private HashMap<Position, Cell> cells;

}