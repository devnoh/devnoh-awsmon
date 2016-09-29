package devnoh.awsmon.controller;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Regions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.comparator.CompoundComparator;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;

/**
 * Created by devnoh on 9/16/16.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

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
