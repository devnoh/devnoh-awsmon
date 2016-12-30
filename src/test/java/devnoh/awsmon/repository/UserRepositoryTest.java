package devnoh.awsmon.repository;

import devnoh.awsmon.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by devnoh on 9/22/16.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void testSave() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("pass1");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail("user1@domain.com");
        User saved = repository.save(user);
        System.out.println("saved=" + saved);

        User found = repository.findOne(saved.getId());
        System.out.println("found=" + found);
        assertEquals("user1", found.getUsername());
        assertEquals("pass1", found.getPassword());
        assertEquals("First", found.getFirstName());
        assertEquals("Last", found.getLastName());
        assertEquals("user1@domain.com", found.getEmail());
    }

    @Test
    public void testFindByUsername() {
        User user = repository.findByUsername("user");
        System.out.println("user=" + user);
        assertEquals("user", user.getUsername());
        assertEquals("user", user.getPassword());
        assertEquals("User", user.getFirstName());
        assertEquals("Noh", user.getLastName());
        assertEquals("user@domain.com", user.getEmail());
    }
}
