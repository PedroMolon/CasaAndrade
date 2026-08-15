package com.pedromolon.CasaAndrade.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SecurityUtils {

    public Long getCurrentUserId() {
        Jwt jwt = (Jwt) Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getPrincipal();

        assert jwt != null;
        return Long.parseLong(Objects.requireNonNull(jwt.getClaimAsString("userId")));
    }

}
