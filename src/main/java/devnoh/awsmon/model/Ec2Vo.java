package devnoh.awsmon.model;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Tag;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by devnoh on 9/29/16.
 */
public class Ec2Vo {

    /*
    0  : pending
    16 : running
    32 : shutting-down
    48 : terminated
    64 : stopping
    80 : stopped
    */

    private Instance instance;

    public Ec2Vo(Instance instance) {
        this.instance = instance;
    }

    public Instance getInstance() {
        return instance;
    }

    public String getName() {
        if (instance != null) {
            for (Tag tag : instance.getTags()) {
                if (tag.getKey().equals("Name")) {
                    return tag.getValue();
                }
            }
        }
        return "";
    }

    public List<String> getTags() {
        return instance.getTags().stream()
                .filter(t -> !"Name".equals(t.getKey()))
                .map(t -> t.getKey() + ": " + t.getValue())
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getSecurityGroups() {
        return instance.getSecurityGroups().stream()
                .map(g -> g.getGroupName())
                .sorted()
                .collect(Collectors.toList());
    }

    public long getUptimeInDays() {
        if (instance != null) {
            return Duration.ofMillis(System.currentTimeMillis() - instance.getLaunchTime().getTime()).toDays();
        }
        return 0;
    }

}
