package devnoh.awsmon.config;

import devnoh.awsmon.AwsRegions;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by devnoh on 10/5/16.
 */
public class RegionHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return "region".equalsIgnoreCase(parameter.getParameterName());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        String region = webRequest.getParameter("region");
        if (region == null || region.isEmpty()) {
            String rawCookie = webRequest.getHeader("Cookie");
            if (rawCookie != null) {
                String[] cookies = rawCookie.split(";");
                for (String cookie : cookies) {
                    String[] nameValue = cookie.split("=");
                    if (nameValue[0].trim().equalsIgnoreCase("region")) {
                        region = nameValue[1].trim();
                    }
                }
            }
        }
        if (region == null || region.isEmpty()) {
            region = AwsRegions.DEFAULT_REGION;
        }
        return region;
    }
}