package devnoh.awsmon.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import devnoh.awsmon.AwsClients;
import devnoh.awsmon.dto.S3Vo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by devnoh on 10/30/16.
 */
@Controller
@RequestMapping("/s3")
public class S3Controller {

    private static final Logger logger = LoggerFactory.getLogger(S3Controller.class);

    private AmazonS3 s3Client = null;

    public S3Controller() {
        s3Client = AwsClients.createS3Client();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String list(String region, // get from HandlerMethodArgumentResolver
                       Model model, HttpServletRequest request, HttpServletResponse response) {
        List<Bucket> buckets = s3Client.listBuckets();
        logger.debug("buckets.size() = " + buckets.size());

        List<S3Vo> s3List = convertToS3VoList(buckets);

        model.addAttribute("s3List", s3List);
        model.addAttribute("updated", new Date());
        model.addAttribute("region", region);
        return "s3";
    }

    private List<S3Vo> convertToS3VoList(List<Bucket> buckets) {
        List<S3Vo> s3List = buckets.stream()
                .map(i -> convertToS3Vo(i))
                .sorted(new Comparator<S3Vo>() {
                    @Override
                    public int compare(S3Vo o1, S3Vo o2) {
                        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                    }
                })
                .collect(Collectors.toList());
        return s3List;
    }

    private S3Vo convertToS3Vo(Bucket bucket) {
        //logger.debug("name = " + bucket.getName());
        int itemCount = 0;
        long totalSize = 0;
        ObjectListing objects = s3Client.listObjects(bucket.getName());
        do {
            for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                totalSize += objectSummary.getSize();
                itemCount++;
            }
            objects = s3Client.listNextBatchOfObjects(objects);
        } while (objects.isTruncated());

        S3Vo s3Vo = new S3Vo();
        s3Vo.setName(bucket.getName());
        s3Vo.setCreateTime(bucket.getCreationDate());
        s3Vo.setItemCount(itemCount);
        s3Vo.setTotalSize(totalSize);
        return s3Vo;
    }
}
