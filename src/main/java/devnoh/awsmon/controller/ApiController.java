package devnoh.awsmon.controller;

import devnoh.awsmon.AwsRegions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devnoh on 9/29/16.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @RequestMapping(value = "/regions", method = RequestMethod.GET)
    public List<RegionVo> getRegions() {
        AwsRegions[] regions = AwsRegions.values();
        List<RegionVo> list = new ArrayList<>();
        for (AwsRegions region : regions) {
            RegionVo vo = new RegionVo();
            vo.code = region.getCode();
            vo.name = region.getName();
            list.add(vo);
        }
        return list;
    }

    class RegionVo {
        String code;
        String name;

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
