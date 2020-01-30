package kubys.Map.Model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class LandPlot extends Cell {

    boolean isDiggable;

}