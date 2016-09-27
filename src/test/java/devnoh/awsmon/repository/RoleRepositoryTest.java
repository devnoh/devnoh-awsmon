package devnoh.awsmon.repository;

import devnoh.awsmon.entity.Role;
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
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository repository;

    @Test
    public void testSave() {
        Role role = new Role();
        role.setName("ROLE_TEST");
        role.setDescription("Tester");
        Role saved = repository.save(role);
        System.out.println("saved=" + saved);

        Role found = repository.findOne(saved.getId());
        System.out.println("found=" + found);
        assertEquals("ROLE_TEST", found.getName());
        assertEquals("Tester", found.getDescription());
    }

    @Test
    public void testFindByName() {
        Role role = repository.findByName("ROLE_USER");
        System.out.println("role=" + role);
        assertEquals("ROLE_USER", role.getName());
        assertEquals("User", role.getDescription());
    }
}
