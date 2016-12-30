package devnoh.awsmon.dto;

/**
 * Created by devnoh on 9/29/16.
 */
public class InstanceVo {

    private String id;
    private String name;

    public InstanceVo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
