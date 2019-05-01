package kubys.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {

    private String email;
    private String username;
    private String passwordHash;

    private Player [] players;

}