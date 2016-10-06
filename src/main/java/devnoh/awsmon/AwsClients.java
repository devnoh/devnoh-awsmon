package devnoh.awsmon;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;

/**
 * Created by devnoh on 9/30/16.
 */
public class AwsClients {

    private static AwsClients instance;

    public static AwsClients getInstance() {
        if (instance == null) {
            synchronized (AwsClients.class) {
                instance = new AwsClients();
            }
        }
        return instance;
    }

    public static String getEc2Endpoint(String region) {
        return Region.getRegion(Regions.fromName(region)).getServiceEndpoint(AmazonEC2.ENDPOINT_PREFIX);
    }

    public static String getElbEndpoint(String region) {
        return Region.getRegion(Regions.fromName(region)).getServiceEndpoint(AmazonElasticLoadBalancing.ENDPOINT_PREFIX);
    }

}
