package com.example.userorder.support;

import com.example.userorder.entity.User;
import com.example.userorder.security.CustomUserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.test.util.ReflectionTestUtils;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        User user = User.createGeneralUser(
                annotation.loginId(),
                annotation.password(),
                annotation.name(),
                annotation.age()
        );
        ReflectionTestUtils.setField(user, "id", annotation.userId());

        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
