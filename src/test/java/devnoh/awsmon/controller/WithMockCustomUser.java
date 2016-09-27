package devnoh.awsmon.controller;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by devnoh on 9/23/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default "user";

    String password() default "pass";

    String firstName() default "User";

    String lastName() default "Noh";

    String email() default "user@domain.com";

    String[] roles() default { "USER" };
}
