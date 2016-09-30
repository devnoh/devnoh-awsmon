package devnoh.awsmon.model;

/**
 * Created by sehnoh on 9/29/16.
 */
public class RegionVo {

    private String code;
    private String name;

    public RegionVo(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
