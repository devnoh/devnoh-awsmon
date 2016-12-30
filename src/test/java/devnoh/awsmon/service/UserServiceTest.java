package devnoh.awsmon.service;

import devnoh.awsmon.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Created by devnoh on 9/23/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void testLoadUserByUsername() throws Exception {
        User user = (User)userService.loadUserByUsername("admin");
        System.out.println("user=" + user);
        assertNotNull(user);
    }

}
