package devnoh.awsmon.service;

import devnoh.awsmon.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by devnoh on 9/16/16.
 */
public interface UserService extends UserDetailsService {

    User saveUser(User user);

}
