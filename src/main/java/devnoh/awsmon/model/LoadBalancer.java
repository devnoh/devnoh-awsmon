/*
 * @(#)LoadBalancer.java
 *
 * Copyright (c) 2014 KW iTech, Inc.
 * All rights reserved.
 */
package devnoh.awsmon.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class desciption goes here.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 * @version 1.0
 */
public class LoadBalancer {

	private String name;
	private String dnsName;
	private String scheme;
	private List<String> portConfigs;
	private Integer instanceCount;
	private List<String> instanceIds;
	private List<Ec2Instance> ec2Instances;

	public LoadBalancer() {
		instanceIds = new ArrayList<String>();
	}

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

	public List<String> getPortConfigs() {
		return portConfigs;
	}

	public void setPortConfigs(List<String> portConfigs) {
		this.portConfigs = portConfigs;
	}

	public Integer getInstanceCount() {
		return instanceCount;
	}

	public void setInstanceCount(Integer instanceCount) {
		this.instanceCount = instanceCount;
	}

	public List<String> getInstanceIds() {
		return instanceIds;
	}

	public void setInstanceIds(List<String> instanceIds) {
		this.instanceIds = instanceIds;
	}

	public List<Ec2Instance> getEc2Instances() {
		return ec2Instances;
	}

	public void setEc2Instances(List<Ec2Instance> ec2Instances) {
		this.ec2Instances = ec2Instances;
	}
}
