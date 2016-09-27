package devnoh.awsmon.controller;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by devnoh on 9/23/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default "user";

    String password() default "pass";

    String firstName() default "Sehwan";

    String lastName() default "Noh";

    String email() default "user@domain.com";

    String[] roles() default { "USER" };
}
