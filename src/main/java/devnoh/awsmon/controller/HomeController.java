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

    @RequestMapping(value = "/api/regions", method = RequestMethod.GET)
    @ResponseBody
    public List<RegionVo> getRegions() {
        AwsRegions[] regions = AwsRegions.values();
        List<RegionVo> list = Arrays.stream(regions).map(r -> new RegionVo(r.getCode(), r.getName())).collect(Collectors.toList());
        /*
        List<RegionVo> list = new ArrayList<>();
        for (AwsRegions region : regions) {
            RegionVo vo = new RegionVo();
            vo.code = region.getCode();
            vo.name = region.getName();
            list.add(vo);
        }
        */
        return list;
    }

    class RegionVo {
        String code;
        String name;

        RegionVo(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
