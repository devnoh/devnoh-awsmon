/*
 * @(#)Ec2Controller.java
 *
 * Copyright (c) 2012 KW iTech, Inc.
 * All rights reserved.
 */
package devnoh.awsmon.controller;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient;
import com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersResult;
import com.amazonaws.services.elasticloadbalancing.model.Listener;
import com.amazonaws.services.elasticloadbalancing.model.ListenerDescription;
import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;
import devnoh.awsmon.AwsClients;
import devnoh.awsmon.AwsRegions;
import devnoh.awsmon.model.Ec2Vo;
import devnoh.awsmon.model.ElbVo;
import devnoh.awsmon.model.InstanceVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by devnoh on 9/16/16.
 */
@Controller
@RequestMapping("/elb")
public class ElbController {

    private static final Logger logger = LoggerFactory.getLogger(ElbController.class);

    private static AmazonElasticLoadBalancing elbClient = null;

    public ElbController() {
        if (elbClient == null) {
            AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();
            elbClient = new AmazonElasticLoadBalancingClient(credentialsProvider);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    //public String list(@RequestParam(defaultValue = AwsRegions.DEFAULT_REGION) String region,
    public String list(String region, // get from HandlerMethodArgumentResolver
                       Model model, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("region=" + region);
        elbClient.setEndpoint(AwsClients.getElbEndpoint(region));

        DescribeLoadBalancersResult describeLoadBalancersRequest = elbClient.describeLoadBalancers();
        List<LoadBalancerDescription> descriptions = describeLoadBalancersRequest.getLoadBalancerDescriptions();
        List<ElbVo> elbList = convertToElbVoList(descriptions);

        model.addAttribute("elbList", elbList);
        model.addAttribute("updated", new Date());
        model.addAttribute("region", region);
        return "elb";
    }

    private List<ElbVo> convertToElbVoList(List<LoadBalancerDescription> descriptions) {
        List<ElbVo> elbList = descriptions.stream()
                .map(d -> convertToElbVo(d))
                .sorted(new Comparator<ElbVo>() {
                    @Override
                    public int compare(ElbVo o1, ElbVo o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                })
                .collect(Collectors.toList());
        return elbList;
    }

    private ElbVo convertToElbVo(LoadBalancerDescription description) {

        List<String> portConfigs = description.getListenerDescriptions().stream()
                .map(d -> String.format("%s (%s) forwarding to %s (%s)",
                        d.getListener().getLoadBalancerPort(), d.getListener().getProtocol(),
                        d.getListener().getInstancePort(), d.getListener().getInstanceProtocol()))
                .sorted()
                .collect(Collectors.toList());

        List<InstanceVo> instances = description.getInstances().stream()
                .map(i -> new InstanceVo(i.getInstanceId(), AwsClients.getEc2Cache().get(i.getInstanceId())))
                .sorted(new Comparator<InstanceVo>() {
                    @Override
                    public int compare(InstanceVo o1, InstanceVo o2) {
                        if (o1.getName() != null) {
                            return o1.getName().compareTo(o2.getName());
                        } else {
                            return o1.getId().compareTo(o2.getId());
                        }
                    }
                })
                .collect(Collectors.toList());

        ElbVo elbVo = new ElbVo();
        elbVo.setName(description.getLoadBalancerName());
        elbVo.setDnsName(description.getDNSName());
        elbVo.setScheme(description.getScheme());
        elbVo.setCreatedTime(description.getCreatedTime());
        elbVo.setPortConfigs(portConfigs);
        elbVo.setInstances(instances);
        return elbVo;
    }

    /////////////////////
    // API
    /////////////////////

    @RequestMapping("/api/descriptions")
    @ResponseBody
    public List<LoadBalancerDescription> getElbDescriptionList(@RequestParam(defaultValue = AwsRegions.DEFAULT_REGION) String region) {
        logger.debug("region=" + region);
        elbClient.setEndpoint(AwsClients.getElbEndpoint(region));

        DescribeLoadBalancersResult describeLoadBalancersRequest = elbClient.describeLoadBalancers();
        return describeLoadBalancersRequest.getLoadBalancerDescriptions();
    }

    @RequestMapping("/api/list")
    @ResponseBody
    public List<ElbVo> getElbVoList(@RequestParam(defaultValue = AwsRegions.DEFAULT_REGION) String region) {
        logger.debug("region=" + region);
        elbClient.setEndpoint(AwsClients.getElbEndpoint(region));

        DescribeLoadBalancersResult describeLoadBalancersRequest = elbClient.describeLoadBalancers();
        List<LoadBalancerDescription> descriptions = describeLoadBalancersRequest.getLoadBalancerDescriptions();
        return convertToElbVoList(descriptions);
    }

}
