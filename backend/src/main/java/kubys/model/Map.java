package kubys.model;

import kubys.model.common.Position;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Builder
@Data
public class Map {

    //TODO : merge this and MapService

    private HashMap<Position, Cell> cells;

    public Map() {
    }
}