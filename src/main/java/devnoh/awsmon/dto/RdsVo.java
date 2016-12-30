package devnoh.awsmon.dto;

import java.util.Date;

/**
 * Created by devnoh on 10/30/16.
 */
public class RdsVo {

    private String name;
    private String instanceClass;
    private String status;
    private String engine;
    private String engineVersion;
    private String dbName;
    private Integer storage;
    private Integer iops;
    private Date createTime;
    private Date latestRestorableTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstanceClass() {
        return instanceClass;
    }

    public void setInstanceClass(String instanceClass) {
        this.instanceClass = instanceClass;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getEngineVersion() {
        return engineVersion;
    }

    public void setEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public Integer getStorage() {
        return storage;
    }

    public void setStorage(Integer storage) {
        this.storage = storage;
    }

    public Integer getIops() {
        return iops;
    }

    public void setIops(Integer iops) {
        this.iops = iops;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLatestRestorableTime() {
        return latestRestorableTime;
    }

    public void setLatestRestorableTime(Date latestRestorableTime) {
        this.latestRestorableTime = latestRestorableTime;
    }
}
