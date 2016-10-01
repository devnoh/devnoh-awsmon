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
import com.amazonaws.regions.Region;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;
import devnoh.awsmon.AwsRegions;
import devnoh.awsmon.model.Ec2Vo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by devnoh on 9/16/16.
 */
@Controller
@RequestMapping("/ec2")
public class Ec2Controller {

    private static final Logger logger = LoggerFactory.getLogger(Ec2Controller.class);

    private static AmazonEC2 ec2Client = null;

    public Ec2Controller() {
        if (ec2Client == null) {
            AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();
            ec2Client = new AmazonEC2Client(credentialsProvider);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String list(@CookieValue(value = "region", defaultValue = "") String region,
                       Model model, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(region)) {
            region = AwsRegions.DEFAULT_REGION.getCode();
            response.addCookie(new Cookie("region", region));
        }
        logger.debug("region=" + region);

        String endpoint = Region.getRegion(Regions.fromName(region)).getServiceEndpoint(AmazonEC2.ENDPOINT_PREFIX);
        ec2Client.setEndpoint(endpoint);

        DescribeInstancesResult describeInstancesResult = ec2Client.describeInstances();
        List<Reservation> reservations = describeInstancesResult.getReservations();
        List<Ec2Vo> ec2List = convertToEc2VoList(reservations);

        long runningCount = ec2List.stream().filter(i -> i.getInstance().getState().getCode() == 16).count(); // running

        model.addAttribute("ec2List", ec2List);
        model.addAttribute("runningCount", runningCount);
        model.addAttribute("updated", new Date());
        return "ec2";
    }

    private List<Ec2Vo> convertToEc2VoList(List<Reservation> reservations) {
        /*
        List<Instance> instances = new ArrayList<Instance>();
        for (Reservation reservation : reservations) {
            instances.addAll(reservation.getInstances());
        }
        */
        List<Ec2Vo> ec2List = reservations.stream()
                .map(r -> new Ec2Vo(r.getInstances().get(0)))
                .sorted(new Comparator<Ec2Vo>() {
                    @Override
                    public int compare(Ec2Vo o1, Ec2Vo o2) {
                        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                    }
                })
                .collect(Collectors.toList());
        return ec2List;
    }

    /*
    @RequestMapping("/api/reservations")
    @ResponseBody
    public List<Reservation> getEc2ReservationList() {
        DescribeInstancesResult describeInstancesResult = ec2Client.describeInstances();
        return describeInstancesResult.getReservations();
    }

    @RequestMapping("/api/instances")
    @ResponseBody
    public List<Instance> getEc2InstanceList(HttpServletRequest request) {
        String region = ServletRequestUtils.getStringParameter(request, "region", "us-west-1");
        logger.debug("region=" + region);

        String endpoint = Region.getRegion(Regions.fromName(region)).getServiceEndpoint(AmazonEC2.ENDPOINT_PREFIX);
        ec2Client.setEndpoint(endpoint);

        DescribeInstancesResult describeInstancesResult = ec2Client.describeInstances();
        List<Reservation> reservations = describeInstancesResult.getReservations();
        List<Instance> instances = new ArrayList<Instance>();
        for (Reservation reservation : reservations) {
            instances.addAll(reservation.getInstances());
        }
        return instances;
    }
    */

    @RequestMapping("/api/list")
    @ResponseBody
    public List<Ec2Vo> getEc2List(HttpServletRequest request) {
        String region = ServletRequestUtils.getStringParameter(request, "region", "us-west-1");
        logger.debug("region=" + region);

        String endpoint = Region.getRegion(Regions.fromName(region)).getServiceEndpoint(AmazonEC2.ENDPOINT_PREFIX);
        ec2Client.setEndpoint(endpoint);

        DescribeInstancesResult describeInstancesResult = ec2Client.describeInstances();
        List<Reservation> reservations = describeInstancesResult.getReservations();
        return convertToEc2VoList(reservations);
    }

    /*
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/api/start", method = RequestMethod.GET)
    @ResponseBody
    public void startEc2Instance(@RequestParam String region, @RequestParam String instanceId) {
        logger.debug("region=" + region);
        logger.debug("instanceId=" + instanceId);
        startEc2Instances(region, instanceId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/api/stop", method = RequestMethod.GET)
    @ResponseBody
    public void stopEc2Instance(@RequestParam String region, @RequestParam String instanceId) {
        stopEc2Instances(region, instanceId);
    }
    */

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/api/start", method = RequestMethod.POST)
    @ResponseBody
    public void startEc2Instance(@RequestBody Map<String, String> instance) {
        logger.debug("instance={}", instance);
        startEc2Instances(instance.get("region"), instance.get("instanceId"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/api/stop", method = RequestMethod.POST)
    @ResponseBody
    public void stopEc2Instance(@RequestBody Map<String, String> instance) {
        logger.debug("instance={}", instance);
        stopEc2Instances(instance.get("region"), instance.get("instanceId"));
    }

    public void startEc2Instances(String region, String... instanceIds) {
        logger.debug("startEc2Instances()...");
        logger.debug("region={}, intanceIds={}", region, instanceIds);
        String endpoint = Region.getRegion(Regions.fromName(region)).getServiceEndpoint(AmazonEC2.ENDPOINT_PREFIX);
        ec2Client.setEndpoint(endpoint);

        StartInstancesRequest startRequest = new StartInstancesRequest().withInstanceIds(instanceIds);
        StartInstancesResult startResult = ec2Client.startInstances(startRequest);

        startResult.getStartingInstances().stream().forEach(s -> logger.debug("{}: {} -> {}",
                s.getInstanceId(), s.getPreviousState().getName(), s.getCurrentState().getName()));
    }

    public void stopEc2Instances(String region, String... instanceIds) {
        logger.debug("stopEc2Instances()...");
        logger.debug("region={}, intanceIds={}", region, instanceIds);
        String endpoint = Region.getRegion(Regions.fromName(region)).getServiceEndpoint(AmazonEC2.ENDPOINT_PREFIX);
        ec2Client.setEndpoint(endpoint);

        StopInstancesRequest stopRequest = new StopInstancesRequest().withInstanceIds(instanceIds);
        StopInstancesResult stopResult = ec2Client.stopInstances(stopRequest);

        stopResult.getStoppingInstances().stream().forEach(s -> logger.debug("{}: {} -> {}",
                s.getInstanceId(), s.getPreviousState().getName(), s.getCurrentState().getName()));
    }

    public void rebootEc2Instances(String region, String... instanceIds) {
        logger.debug("rebootEc2Instances()...");
        logger.debug("region={}, intanceIds={}", region, instanceIds);
        String endpoint = Region.getRegion(Regions.fromName(region)).getServiceEndpoint(AmazonEC2.ENDPOINT_PREFIX);
        ec2Client.setEndpoint(endpoint);

        RebootInstancesRequest rebootRequest = new RebootInstancesRequest().withInstanceIds(instanceIds);
        ec2Client.rebootInstances(rebootRequest);
    }
}
