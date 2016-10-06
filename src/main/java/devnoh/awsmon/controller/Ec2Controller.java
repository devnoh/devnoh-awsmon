/*
 * @(#)Ec2Controller.java
 *
 * Copyright (c) 2012 KW iTech, Inc.
 * All rights reserved.
 */
package devnoh.awsmon.controller;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;
import devnoh.awsmon.AwsClients;
import devnoh.awsmon.AwsRegions;
import devnoh.awsmon.model.Ec2Vo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    //public String list(@RequestParam(defaultValue = AwsRegions.DEFAULT_REGION) String region,
    public String list(String region, // get from HandlerMethodArgumentResolver
                       Model model, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("region=" + region);
        ec2Client.setEndpoint(AwsClients.getEc2Endpoint(region));

        DescribeInstancesResult describeInstancesResult = ec2Client.describeInstances();
        List<Reservation> reservations = describeInstancesResult.getReservations();
        List<Ec2Vo> ec2List = convertToEc2VoList(reservations);
        long runningCount = ec2List.stream().filter(i -> i.getStateCode() == 16).count(); // running

        model.addAttribute("ec2List", ec2List);
        model.addAttribute("runningCount", runningCount);
        model.addAttribute("updated", new Date());
        model.addAttribute("region", region);
        return "ec2";
    }

    private List<Ec2Vo> convertToEc2VoList(List<Reservation> reservations) {
        /*
        List<Instance> instances = new ArrayList<Instance>();
        for (Reservation reservation : reservations) {
            instances.addAll(reservation.getInstances());
        }
        */
        List<Instance> instances = new ArrayList<Instance>();
        reservations.forEach(r -> instances.addAll(r.getInstances()));

        List<Ec2Vo> ec2List = instances.stream()
                .map(i -> convertToEc2Vo(i))
                .sorted(new Comparator<Ec2Vo>() {
                    @Override
                    public int compare(Ec2Vo o1, Ec2Vo o2) {
                        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                    }
                })
                .collect(Collectors.toList());
        return ec2List;
    }

    private Ec2Vo convertToEc2Vo(Instance instance) {
        String name = instance.getTags().stream()
                .filter(t -> t.getKey().equals("Name"))
                .findFirst().map(t -> t.getValue())
                .orElse("");

        List<Map.Entry> tags = instance.getTags().stream()
                .filter(t -> !"Name".equals(t.getKey()))
                .map(t -> new AbstractMap.SimpleEntry<String, String>(t.getKey(), t.getValue()))
                .sorted()
                .collect(Collectors.toList());

        List<String> securityGroups = instance.getSecurityGroups().stream()
                .map(g -> g.getGroupName())
                .sorted()
                .collect(Collectors.toList());

        Ec2Vo ec2Vo = new Ec2Vo();
        ec2Vo.setName(name);
        ec2Vo.setInstanceId(instance.getInstanceId());
        ec2Vo.setInstanceType(instance.getInstanceType());
        ec2Vo.setStateCode(instance.getState().getCode());
        ec2Vo.setStateName(instance.getState().getName());
        ec2Vo.setPlatform(instance.getPlatform());
        ec2Vo.setPrivateIp(instance.getPrivateIpAddress());
        ec2Vo.setPublicIp(instance.getPublicIpAddress());
        ec2Vo.setLaunchTime(instance.getLaunchTime());
        ec2Vo.setSecurityGroups(securityGroups);
        ec2Vo.setTags(tags);
        return ec2Vo;
    }

    /////////////////////
    // API
    /////////////////////

    @RequestMapping("/api/reservations")
    @ResponseBody
    public List<Reservation> getEc2ReservationList(@RequestParam(defaultValue = AwsRegions.DEFAULT_REGION) String region) {
        logger.debug("region=" + region);
        ec2Client.setEndpoint(AwsClients.getEc2Endpoint(region));

        DescribeInstancesResult describeInstancesResult = ec2Client.describeInstances();
        return describeInstancesResult.getReservations();
    }

    @RequestMapping("/api/list")
    @ResponseBody
    public List<Ec2Vo> getEc2VoList(@RequestParam(defaultValue = AwsRegions.DEFAULT_REGION) String region) {
        logger.debug("region=" + region);
        ec2Client.setEndpoint(AwsClients.getEc2Endpoint(region));

        DescribeInstancesResult describeInstancesResult = ec2Client.describeInstances();
        List<Reservation> reservations = describeInstancesResult.getReservations();
        return convertToEc2VoList(reservations);
    }

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

    private void startEc2Instances(String region, String... instanceIds) {
        logger.debug("startEc2Instances()...");
        logger.debug("region={}, intanceIds={}", region, instanceIds);
        ec2Client.setEndpoint(AwsClients.getEc2Endpoint(region));

        StartInstancesRequest startRequest = new StartInstancesRequest().withInstanceIds(instanceIds);
        StartInstancesResult startResult = ec2Client.startInstances(startRequest);

        startResult.getStartingInstances().stream().forEach(s -> logger.debug("{}: {} -> {}",
                s.getInstanceId(), s.getPreviousState().getName(), s.getCurrentState().getName()));
    }

    private void stopEc2Instances(String region, String... instanceIds) {
        logger.debug("stopEc2Instances()...");
        logger.debug("region={}, intanceIds={}", region, instanceIds);
        ec2Client.setEndpoint(AwsClients.getEc2Endpoint(region));

        StopInstancesRequest stopRequest = new StopInstancesRequest().withInstanceIds(instanceIds);
        StopInstancesResult stopResult = ec2Client.stopInstances(stopRequest);

        stopResult.getStoppingInstances().stream().forEach(s -> logger.debug("{}: {} -> {}",
                s.getInstanceId(), s.getPreviousState().getName(), s.getCurrentState().getName()));
    }

    private void rebootEc2Instances(String region, String... instanceIds) {
        logger.debug("rebootEc2Instances()...");
        logger.debug("region={}, intanceIds={}", region, instanceIds);
        ec2Client.setEndpoint(AwsClients.getEc2Endpoint(region));

        RebootInstancesRequest rebootRequest = new RebootInstancesRequest().withInstanceIds(instanceIds);
        ec2Client.rebootInstances(rebootRequest);
    }
}
