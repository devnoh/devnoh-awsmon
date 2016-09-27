package devnoh.awsmon.repository;

import devnoh.awsmon.entity.Role;
import devnoh.awsmon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by devnoh on 9/15/16.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}
