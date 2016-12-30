package devnoh.awsmon.dto;

import java.util.Date;

/**
 * Created by devnoh on 10/30/16.
 */
public class S3Vo {

    private String name;
    private Date createTime;
    private Integer itemCount;
    private Long totalSize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }
}
