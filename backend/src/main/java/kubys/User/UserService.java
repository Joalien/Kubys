package kubys.User;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserDao userDao;

    public User findById(String userId) {
        return userDao.findById(userId).get();
    }
}
