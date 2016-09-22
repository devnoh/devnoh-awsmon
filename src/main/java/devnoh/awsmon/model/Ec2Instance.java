/*
 * @(#)Ec2Instance.java
 *
 * Copyright (c) 2014 KW iTech, Inc.
 * All rights reserved.
 */
package devnoh.awsmon.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class desciption goes here.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 * @version 1.0
 */
public class Ec2Instance {

	private String name;
	private String role;
	private String instanceId;
	private String instanceType;
	private Integer stateCode;
	private String stateName;
	private String platform;
	private String privateIp;
	private String publicIp;
	private Date launchTime;
	private String elapse;
	private List<String> securityGroups;
	private List<LoadBalancer> loadBalancers;

	public Ec2Instance() {
		securityGroups = new ArrayList<String>();
		loadBalancers = new ArrayList<LoadBalancer>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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

	public String getElapse() {
		return elapse;
	}

	public void setElapse(String elapse) {
		this.elapse = elapse;
	}

	public List<String> getSecurityGroups() {
		return securityGroups;
	}

	public void setSecurityGroups(List<String> securityGroups) {
		this.securityGroups = securityGroups;
	}

	public List<LoadBalancer> getLoadBalancers() {
		return loadBalancers;
	}

	public void setLoadBalancers(List<LoadBalancer> loadBalancers) {
		this.loadBalancers = loadBalancers;
	}
}
