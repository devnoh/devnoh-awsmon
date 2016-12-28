package devnoh.awsmon.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by devnoh on 10/22/16.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @AfterThrowing(pointcut = "execution(* devnoh.awsmon.service..*.*(..))", throwing = "ex")
    public void logException(JoinPoint jp, Throwable ex) throws Throwable {
        logger.error("----------------------error----------------------");
        //logger.error(ExceptionUtils.getStackTrace(ex));
        logger.error(ex.toString(), ex);
        throw ex;
    }
}
