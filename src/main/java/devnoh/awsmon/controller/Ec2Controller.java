/*
 * @(#)Ec2Controller.java
 *
 * Copyright (c) 2012 KW iTech, Inc.
 * All rights reserved.
 */
package devnoh.awsmon.controller;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.*;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;
import devnoh.awsmon.model.Ec2Instance;
import devnoh.awsmon.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Class description goes here.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 * @version 1.0
 */
@Controller
@RequestMapping("/ec2")
public class Ec2Controller {

	private static final Logger logger = LoggerFactory.getLogger(Ec2Controller.class);

	private static AmazonEC2 ec2;
	private static Map<String, List<Ec2Instance>> ec2ListMap;
	private static boolean ec2Loading = false;
	private static long ec2LastLoadTime = 0L;

	public Ec2Controller() {
		AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();
		ec2 = new AmazonEC2Client(credentialsProvider);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String list(Model model, HttpServletRequest request) {
		String region = ServletRequestUtils.getStringParameter(request, "region", "us-west-1");
		logger.debug("region=" + region);

		List<Ec2Instance> ec2List = loadEc2InstanceList(region);
		int runningCount = 0;
		for (Ec2Instance ec2Info : ec2List) {
			if (ec2Info.getStateName().equals("running")) {
				runningCount++;
			}
		}
		logger.debug("runningCount = " + runningCount);

		model.addAttribute("ec2List", ec2List);
		model.addAttribute("runningCount", runningCount);
		model.addAttribute("updated", new Date());
		model.addAttribute("region", region);
		return "ec2";
	}

	public synchronized List<Ec2Instance> loadEc2InstanceList(String region) {
		// if (ec2Loading || (System.currentTimeMillis() - ec2LastLoadTime < MINIMUM_INTERVAL)) {
		// return ec2ListMap.get(region);
		// }
		ec2Loading = true;
		logger.debug("loadEc2InstanceList()..." + region);

		String endpoint = getAmazonEC2Endpoint(region);
		logger.debug("endpoint=" + endpoint);
		ec2.setEndpoint(endpoint);

		DescribeInstancesResult describeInstancesResult = ec2.describeInstances();
		List<Reservation> reservations = describeInstancesResult.getReservations();
		Set<Instance> instances = new HashSet<Instance>();
		for (Reservation reservation : reservations) {
			instances.addAll(reservation.getInstances());
		}
		logger.debug("instances.size() = " + instances.size());

		List<Ec2Instance> ec2List = new ArrayList<Ec2Instance>();
		// int runningCount = 0;
		for (Instance instance : instances) {
			// logger.debug("--------------------------------------");
			// logger.debug(instance.toString());
			String name = "";
			String role = "";
			for (Tag tag : instance.getTags()) {
				// logger.debug(tag.getKey() + "=" + tag.getValue());
				if (tag.getKey().equals("Name")) {
					name = tag.getValue();
					logger.debug("name = " + name);
				} else if (tag.getKey().equals("Role")) {
					role = tag.getValue();
				}
			}

			List<String> securityGroups = new ArrayList<String>();
			for (GroupIdentifier group : instance.getSecurityGroups()) {
				securityGroups.add(group.getGroupName());
			}

			Ec2Instance ec2Info = new Ec2Instance();
			ec2Info.setName(name);
			ec2Info.setRole(role);
			ec2Info.setInstanceId(instance.getInstanceId());
			ec2Info.setInstanceType(instance.getInstanceType());
			ec2Info.setStateCode(instance.getState().getCode());
			ec2Info.setStateName(instance.getState().getName());
			ec2Info.setPlatform(instance.getPlatform());
			ec2Info.setPrivateIp(instance.getPrivateIpAddress());
			ec2Info.setPublicIp(instance.getPublicIpAddress());
			ec2Info.setLaunchTime(instance.getLaunchTime());
			ec2Info.setElapse(DateUtil.getElapsedTimeString2(System.currentTimeMillis()
					- instance.getLaunchTime().getTime()));
			ec2Info.setSecurityGroups(securityGroups);
			// if (instance.getState().getCode().equals("running")) {
			// runningCount++;
			// }
			ec2List.add(ec2Info);
		}
		// logger.debug("runningCount = " + runningCount);

		Collections.sort(ec2List, new Comparator<Ec2Instance>() {
			public int compare(Ec2Instance obj1, Ec2Instance obj2) {
				return obj1.getName().toLowerCase().compareTo(obj2.getName().toLowerCase());
			}
		});

		ec2Loading = false;
		ec2LastLoadTime = System.currentTimeMillis();
		return ec2List;
	}

	public static String getAmazonEC2Endpoint(String regionName) {
		com.amazonaws.regions.Region region = com.amazonaws.regions.Region.getRegion(Regions.fromName(regionName));
		return region.getServiceEndpoint(ServiceAbbreviations.EC2);
	}

}
