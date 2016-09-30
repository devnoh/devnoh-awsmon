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
import devnoh.awsmon.model.Ec2Instance;
import devnoh.awsmon.model.Ec2Vo;
import devnoh.awsmon.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

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

    private static AmazonEC2 ec2 = null;

    public Ec2Controller() {
        if (ec2 == null) {
            AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();
            ec2 = new AmazonEC2Client(credentialsProvider);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) {
        String region = ServletRequestUtils.getStringParameter(request, "region", "us-west-1");
        logger.debug("region=" + region);

        String endpoint = Region.getRegion(Regions.fromName(region)).getServiceEndpoint(AmazonEC2.ENDPOINT_PREFIX);
        ec2.setEndpoint(endpoint);

        DescribeInstancesResult describeInstancesResult = ec2.describeInstances();
        List<Reservation> reservations = describeInstancesResult.getReservations();
        List<Ec2Vo> ec2List = convertToEc2VoList(reservations);

		/*
        0  : pending
		16 : running
		32 : shutting-down
		48 : terminated
		64 : stopping
		80 : stopped
		*/
        long runningCount = ec2List.stream().filter(i -> i.getInstance().getState().getCode() == 16).count(); // running

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
        DescribeInstancesResult describeInstancesResult = ec2.describeInstances();
        return describeInstancesResult.getReservations();
    }

    @RequestMapping("/api/instances")
    @ResponseBody
    public List<Instance> getEc2InstanceList(HttpServletRequest request) {
        String region = ServletRequestUtils.getStringParameter(request, "region", "us-west-1");
        logger.debug("region=" + region);

        String endpoint = Region.getRegion(Regions.fromName(region)).getServiceEndpoint(AmazonEC2.ENDPOINT_PREFIX);
        ec2.setEndpoint(endpoint);

        DescribeInstancesResult describeInstancesResult = ec2.describeInstances();
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
        ec2.setEndpoint(endpoint);

        DescribeInstancesResult describeInstancesResult = ec2.describeInstances();
        List<Reservation> reservations = describeInstancesResult.getReservations();
        return convertToEc2VoList(reservations);
    }

}
