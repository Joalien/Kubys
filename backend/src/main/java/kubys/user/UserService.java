package kubys.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UserService {

    private UserDao userDao;

    @Transactional
    public User findById(String userId) {
        return userDao.findById(userId).orElseThrow(NoSuchElementException::new);
    }
}
