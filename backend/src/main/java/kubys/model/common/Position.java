package kubys.model.common;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Position {
    private int x;
    private int y;
    private int z;

}
