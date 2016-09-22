package devnoh.awsmon.repository;

import devnoh.awsmon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by devnoh on 9/15/16.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findOneByUsername(String username);

}
