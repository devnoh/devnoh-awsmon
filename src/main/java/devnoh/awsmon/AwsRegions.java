package devnoh.awsmon;

/**
 * Created by devnoh on 9/29/16.
 */
public enum AwsRegions {

    US_EAST_1("us-east-1", "US East (N. Virginia)"),
    US_WEST_1("us-west-1", "US West (N. California)"),
    US_WEST_2("us-west-2", "US West (Oregon)"),
    EU_WEST_1("eu-west-1", "EU (Ireland)"),
    EU_CENTRAL_1("eu-central-1", "EU (Frankfurt)"),
    AP_NORTHEAST_1("ap-northeast-1", "Asia Pacific (Tokyo)"),
    AP_NORTHEAST_2("ap-northeast-2", "Asia Pacific (Seoul)"),
    AP_SOUTHEAST_1("ap-southeast-1", "Asia Pacific (Singapore)"),
    AP_SOUTHEAST_2("ap-southeast-2", "Asia Pacific (Sydney)"),
    AP_SOUTH_1("ap-south-1", "Asia Pacific (Mumbai)"),
    SA_EAST_1("sa-east-1", "South America (São Paulo)");

    public static final AwsRegions DEFAULT_REGION = US_WEST_2;

    private final String code;
    private final String name;

    private AwsRegions(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static AwsRegions fromCode(String code) {
        for (AwsRegions region : AwsRegions.values()) {
            if (code.equals(region.getCode())) {
                return region;
            }
        }
        throw new IllegalArgumentException("Cannot create enum from " + code + " value!");
    }
}
