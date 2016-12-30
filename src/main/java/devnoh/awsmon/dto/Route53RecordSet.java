package devnoh.awsmon.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devnoh on 12/30/16.
 */
public class Route53RecordSet {

    private String name;
    private String type;
    private Long TTL;
    private List<String> records;

    public Route53RecordSet() {
        records = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTTL() {
        return TTL;
    }

    public void setTTL(Long tTL) {
        TTL = tTL;
    }

    public List<String> getRecords() {
        return records;
    }

    public void setRecords(List<String> records) {
        this.records = records;
    }
}
