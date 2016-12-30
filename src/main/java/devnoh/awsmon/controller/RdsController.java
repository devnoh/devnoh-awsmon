package devnoh.awsmon.controller;

import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import devnoh.awsmon.AwsClients;
import devnoh.awsmon.dto.RdsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by devnoh on 10/30/16.
 */
@Controller
@RequestMapping("/rds")
public class RdsController {

    private static final Logger logger = LoggerFactory.getLogger(RdsController.class);

    private AmazonRDS rdsClient = null;

    public RdsController() {
        rdsClient = AwsClients.createRdsClient();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String list(String region, // get from HandlerMethodArgumentResolver
                       Model model, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("region=" + region);
        rdsClient.setEndpoint(AwsClients.getRdsEndpoint(region));

        DescribeDBInstancesResult describeInstancesResult = rdsClient.describeDBInstances();
        List<DBInstance> instances = describeInstancesResult.getDBInstances();
        List<RdsVo> rdsList = convertToRdsVoList(instances);

        model.addAttribute("rdsList", rdsList);
        model.addAttribute("updated", new Date());
        model.addAttribute("region", region);
        return "rds";
    }

    private List<RdsVo> convertToRdsVoList(List<DBInstance> instances) {

        List<RdsVo> rdsList = instances.stream()
                .map(i -> convertToRdsVo(i))
                .sorted(new Comparator<RdsVo>() {
                    @Override
                    public int compare(RdsVo o1, RdsVo o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                })
                .collect(Collectors.toList());
        return rdsList;
    }

    private RdsVo convertToRdsVo(DBInstance instance) {
        RdsVo rdsInfo = new RdsVo();
        rdsInfo.setName(instance.getDBInstanceIdentifier());
        rdsInfo.setStatus(instance.getDBInstanceStatus());
        rdsInfo.setInstanceClass(instance.getDBInstanceClass());
        rdsInfo.setEngine(instance.getEngine());
        rdsInfo.setEngineVersion(instance.getEngineVersion());
        rdsInfo.setDbName(instance.getDBName());
        rdsInfo.setStorage(instance.getAllocatedStorage());
        rdsInfo.setIops(instance.getIops());
        rdsInfo.setCreateTime(instance.getInstanceCreateTime());
        rdsInfo.setLatestRestorableTime(instance.getLatestRestorableTime());
        return rdsInfo;
    }

}
