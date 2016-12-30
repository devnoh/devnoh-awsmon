package devnoh.awsmon.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devnoh on 12/30/16.
 */
public class Route53Domain {

    private String name;
    private String comment;
    private String hostedZoneId;
    private Long recordSetCount;
    private List<Route53RecordSet> recordSets;

    public Route53Domain() {
        recordSets = new ArrayList<Route53RecordSet>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHostedZoneId() {
        return hostedZoneId;
    }

    public void setHostedZoneId(String hostedZoneId) {
        this.hostedZoneId = hostedZoneId;
    }

    public Long getRecordSetCount() {
        return recordSetCount;
    }

    public void setRecordSetCount(Long recordSetCount) {
        this.recordSetCount = recordSetCount;
    }

    public List<Route53RecordSet> getRecordSets() {
        return recordSets;
    }

    public void setRecordSets(List<Route53RecordSet> recordSets) {
        this.recordSets = recordSets;
    }
}
