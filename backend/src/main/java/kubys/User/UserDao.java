package kubys.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
//public interface UserDao extends JpaRepository<User, String> {
public interface UserDao extends CrudRepository<User, String> {


}
