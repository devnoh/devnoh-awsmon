package devnoh.awsmon;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClientBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by devnoh on 9/30/16.
 */
public class AwsClients {

    private static AwsClients instance;
    private static Map<String, String> ec2Cache = new HashMap<>();

    private AwsClients() {
    }

    private static AwsClients getInstance() {
        if (instance == null) {
            synchronized (AwsClients.class) {
                instance = new AwsClients();
            }
        }
        return instance;
    }

    public static AmazonEC2 createEc2Client() {
        return new AmazonEC2Client(new ProfileCredentialsProvider());
    }

    /*
    public static AmazonEC2 createEc2Client(String region) {
        return AmazonEC2ClientBuilder.standard().withRegion(region).build();
    }
    */

    public static AmazonElasticLoadBalancing createElbClient() {
        return new AmazonElasticLoadBalancingClient(new ProfileCredentialsProvider());
    }

    /*
    public static AmazonElasticLoadBalancing createElbClient(String region) {
        return AmazonElasticLoadBalancingClientBuilder.standard().withRegion(region).build();
    }
    */

    public static String getEc2Endpoint(String region) {
        return Region.getRegion(Regions.fromName(region)).getServiceEndpoint(AmazonEC2.ENDPOINT_PREFIX);
    }

    public static String getElbEndpoint(String region) {
        return Region.getRegion(Regions.fromName(region)).getServiceEndpoint(AmazonElasticLoadBalancing.ENDPOINT_PREFIX);
    }

    public static Map<String, String> getEc2Cache() {
        return getInstance().ec2Cache;
    }

}
