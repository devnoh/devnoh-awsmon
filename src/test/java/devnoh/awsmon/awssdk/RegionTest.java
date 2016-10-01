package devnoh.awsmon.awssdk;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Regions;
import devnoh.awsmon.AwsRegions;
import devnoh.awsmon.controller.HomeController;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by devnoh on 9/29/16.
 */
public class RegionTest {

    private static final Logger logger = LoggerFactory.getLogger(RegionTest.class);

    @Test
    public void testRegions() {
        List<Region> regions = RegionUtils.getRegions();
        logger.debug("regions.size=" + regions.size());
        //regions.forEach(r -> logger.debug("{}, {}, {}", r.getName(), r.getDomain(), r.getPartition()));

        regions = regions.stream()
                .filter(r -> "aws".equals(r.getPartition()))
                .collect(Collectors.toList());
        logger.debug("regions.size=" + regions.size());
        //regions.forEach(r -> logger.debug("{}, {}, {}", r.getName(), r.getDomain(), r.getPartition()));

        for (Regions region : Regions.values()) {
            logger.debug(region.getName());
        }

        for (AwsRegions region : AwsRegions.values()) {
            logger.debug("[{}] {}", region.getCode(), region.getName());
        }
    }
}
