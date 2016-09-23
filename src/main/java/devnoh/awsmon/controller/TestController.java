package devnoh.awsmon.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by devnoh on 9/22/16.
 */
@RestController
public class TestController {

    @RequestMapping("/greeting")
    public String greeting() {
        return "Hello World";
    }

}
