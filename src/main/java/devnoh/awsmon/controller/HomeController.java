package devnoh.awsmon.controller;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Regions;
import devnoh.awsmon.AwsRegions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by devnoh on 9/16/16.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /*
    @RequestMapping("/")
    public String index() {
        return home();
    }
    */

    @RequestMapping("/")
    public String home() {
        List<Region> regions = RegionUtils.getRegions();

        logger.debug("regions={}", regions);
        regions.forEach(region -> logger.debug("{}, {}, {}", region.getDomain(), region.getName(), region.getPartition()));

        for (Regions region : Regions.values()) {
            System.out.println(region.getName());
        }

        return "home";
    }

}
