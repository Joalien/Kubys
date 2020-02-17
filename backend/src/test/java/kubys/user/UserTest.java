package kubys.user;


import kubys.player.PlayerService;
import kubys.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
@Slf4j
class UserTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("create a user with some player, then verify that save and fetch user perform on cascade")
    void createSaveAndFetchUser() { // This test is quite bad because it tests hibernate
        //Given
        final int NUMBER_OF_PLAYERS = 3;
        User user = User.builder().uid(UUID.randomUUID().toString()).displayName("Alexandre Dumas").build();
        user.setPlayers(TestHelper.createRandomNewPlayers(NUMBER_OF_PLAYERS, user));
        //When
        userDao.save(user);
        User fetchedUser = userService.findById(user.getUid());
        //Then
        Assertions.assertEquals(NUMBER_OF_PLAYERS, playerService.findPlayersByUser(fetchedUser).size());
    }
}
