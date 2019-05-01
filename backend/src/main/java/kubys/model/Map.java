package kubys.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Builder
@Data
public class Map {

    private HashMap<Position, Cell> cells;

}