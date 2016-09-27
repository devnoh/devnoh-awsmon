package devnoh.awsmon.service;

import devnoh.awsmon.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Created by devnoh on 9/16/16.
 */
public interface UserService extends UserDetailsService {

    User saveUser(User user);

}
