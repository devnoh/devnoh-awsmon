package devnoh.awsmon.service;

import devnoh.awsmon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by devnoh on 9/16/16.
 */
@Service
@Transactional(propagation= Propagation.REQUIRED)
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByUsername(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found.");
        }
        return userDetails;
    }
}
