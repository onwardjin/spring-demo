package com.example.userorder.support;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    long userId() default 1L;

    String loginId() default "testLoginId";

    String password() default "encodedPassword";

    String name() default "testUserName";

    int age() default 100;
}
