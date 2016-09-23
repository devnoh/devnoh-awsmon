package devnoh.awsmon;

import devnoh.awsmon.entity.User;
import devnoh.awsmon.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

/**
 * Created by devnoh on 9/22/16.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryTest.class);

    @Autowired
    private UserRepository repository;

    @Test
    public void saveUser() throws Exception {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("pass1");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail("user1@domain.com");
        User saved = repository.save(user);
        logger.debug("saved={}", saved);

        User found = repository.findOne(saved.getId());
        assertEquals("user1", found.getUsername());
        assertEquals("pass1", found.getPassword());
        assertEquals("First", found.getFirstName());
        assertEquals("Last", found.getLastName());
        assertEquals("user1@domain.com", found.getEmail());
    }
}
