package devnoh.awsmon.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by devnoh on 9/29/16.
 */
public class ElbVo {

    private String name;
    private String dnsName;
    private String scheme;
    private Date createdTime;
    private List<String> portConfigs;
    private List<InstanceVo> instances;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDnsName() {
        return dnsName;
    }

    public void setDnsName(String dnsName) {
        this.dnsName = dnsName;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public List<String> getPortConfigs() {
        return portConfigs;
    }

    public void setPortConfigs(List<String> portConfigs) {
        this.portConfigs = portConfigs;
    }

    public List<InstanceVo> getInstances() {
        return instances;
    }

    public void setInstances(List<InstanceVo> instances) {
        this.instances = instances;
    }
}
