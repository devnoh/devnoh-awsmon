package devnoh.awsmon.controller;

import com.amazonaws.services.route53.AmazonRoute53;
import com.amazonaws.services.route53.model.HostedZone;
import com.amazonaws.services.route53.model.ListHostedZonesResult;
import com.amazonaws.services.route53.model.ListResourceRecordSetsRequest;
import com.amazonaws.services.route53.model.ListResourceRecordSetsResult;
import com.amazonaws.services.route53.model.ResourceRecord;
import com.amazonaws.services.route53.model.ResourceRecordSet;
import devnoh.awsmon.AwsClients;
import devnoh.awsmon.dto.Route53Domain;
import devnoh.awsmon.dto.Route53RecordSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by devnoh on 10/30/16.
 */
@Controller
@RequestMapping("/route53")
public class Route53Controller {

    private static final Logger logger = LoggerFactory.getLogger(Route53Controller.class);

    private AmazonRoute53 route53Client = null;

    public Route53Controller() {
        route53Client = AwsClients.createRoute53Client();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String list(String region, // get from HandlerMethodArgumentResolver
                       Model model, HttpServletRequest request, HttpServletResponse response) {

        ListHostedZonesResult listHostedZoneResult = route53Client.listHostedZones();
        List<HostedZone> hostedZones = listHostedZoneResult.getHostedZones();

        List<Route53Domain> route53List = convertToRoute53DomainList(hostedZones);
        model.addAttribute("route53List", route53List);
        model.addAttribute("updated", new Date());
        model.addAttribute("region", region);
        return "route53";
    }

    private List<Route53Domain> convertToRoute53DomainList(List<HostedZone> hostedZones) {
        List<Route53Domain> domainList = hostedZones.stream()
                .map(i -> convertToRoute53Domain(i))
                .sorted(new Comparator<Route53Domain>() {
                    @Override
                    public int compare(Route53Domain o1, Route53Domain o2) {
                        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                    }
                })
                .collect(Collectors.toList());
        return domainList;
    }

    private Route53Domain convertToRoute53Domain(HostedZone hostedZone) {
        //logger.debug("name = " + hostedZone.getName());
        String hostedZoneId = hostedZone.getId();
        ListResourceRecordSetsRequest resourceRecordSetsRequest = new ListResourceRecordSetsRequest();
        resourceRecordSetsRequest.setHostedZoneId(hostedZoneId);
        ListResourceRecordSetsResult resourceRecordSetsResult = route53Client.listResourceRecordSets(resourceRecordSetsRequest);
        List<ResourceRecordSet> resourceRecordSets = resourceRecordSetsResult.getResourceRecordSets();

        List<Route53RecordSet> recordSets = new ArrayList<Route53RecordSet>();
        for (ResourceRecordSet resourceRecordSet : resourceRecordSets) {
            // logger.debug(resourceRecordSet.toString());
            List<String> records = new ArrayList<String>();
            for (ResourceRecord resourceRecord : resourceRecordSet.getResourceRecords()) {
                records.add(resourceRecord.getValue());
            }
            Route53RecordSet recordSet = new Route53RecordSet();
            recordSet.setName(resourceRecordSet.getName());
            recordSet.setType(resourceRecordSet.getType());
            recordSet.setTTL(resourceRecordSet.getTTL());
            recordSet.setRecords(records);
            recordSets.add(recordSet);
        }

        Route53Domain route53Domain = new Route53Domain();
        route53Domain.setHostedZoneId(hostedZoneId);
        route53Domain.setName(hostedZone.getName());
        route53Domain.setComment(hostedZone.getConfig().getComment());
        route53Domain.setRecordSetCount(hostedZone.getResourceRecordSetCount());
        route53Domain.setRecordSets(recordSets);
        return route53Domain;
    }
}
