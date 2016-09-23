package devnoh.awsmon;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Created by devnoh on 9/22/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServerWebTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testWeb1() throws Exception {
        RestTemplate template = new RestTemplate();
        String body = template.getForObject("http://localhost:{port}", String.class, port);
        System.out.println(body);
    }

    @Test
    public void testWeb2() {
        String body = this.restTemplate.getForObject("/login", String.class);
        System.out.println(body);
    }

}
