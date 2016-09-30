package devnoh.awsmon.controller;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Regions;
import devnoh.awsmon.AwsRegions;
import devnoh.awsmon.model.RegionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by devnoh on 9/16/16.
 */
@Controller
@RequestMapping("/region")
public class RegionController {

    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    @RequestMapping(value = "/api/list", method = RequestMethod.GET)
    @ResponseBody
    public List<RegionVo> getRegions() {
        AwsRegions[] regions = AwsRegions.values();
        return Arrays.stream(regions).map(r -> new RegionVo(r.getCode(), r.getName())).collect(Collectors.toList());
    }
}
