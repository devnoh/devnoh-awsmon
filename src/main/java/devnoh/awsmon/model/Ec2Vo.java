package devnoh.awsmon.model;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Tag;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by devnoh on 9/29/16.
 */
public class Ec2Vo {

    /*
    0  : pending
    16 : running
    32 : shutting-down
    48 : terminated
    64 : stopping
    80 : stopped
    */

    private String name;
    private String instanceId;
    private String instanceType;
    private Integer stateCode;
    private String stateName;
    private String platform;
    private String privateIp;
    private String publicIp;
    private Date launchTime;
    private List<String> securityGroups;
    private List<Map.Entry> tags;

    public Ec2Vo() {
        securityGroups = new ArrayList<String>();
        tags = new ArrayList<Map.Entry>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public Integer getStateCode() {
        return stateCode;
    }

    public void setStateCode(Integer stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPrivateIp() {
        return privateIp;
    }

    public void setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public Date getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Date launchTime) {
        this.launchTime = launchTime;
    }

    public List<String> getSecurityGroups() {
        return securityGroups;
    }

    public void setSecurityGroups(List<String> securityGroups) {
        this.securityGroups = securityGroups;
    }

    public List<Map.Entry> getTags() {
        return tags;
    }

    public void setTags(List<Map.Entry> tags) {
        this.tags = tags;
    }

    public long getUptimeInDays() {
        if (launchTime != null) {
            return Duration.ofMillis(System.currentTimeMillis() - launchTime.getTime()).toDays();
        }
        return 0;
    }
}
