package com.bike.ztd.config;

import java.util.LinkedHashMap;
import java.util.Map;

public class SystemAuthConfig {

    public static boolean isAuthMode(String url) {
        return SUPER_NO_AUTH_URL.containsKey(url);
    }

    public static final Map<String, String> SUPER_NO_AUTH_URL = new LinkedHashMap<>();

    static {
        //swagger
        SUPER_NO_AUTH_URL.put("/swagger-ui.html", "anon");
        SUPER_NO_AUTH_URL.put("/v2/api-docs", "anon");
        SUPER_NO_AUTH_URL.put("/swagger-resources/**", "anon");
        SUPER_NO_AUTH_URL.put("/webjars/**", "anon");
        //login
        SUPER_NO_AUTH_URL.put("/auth/login", "anon");
        SUPER_NO_AUTH_URL.put("/auth/code", "anon");
        SUPER_NO_AUTH_URL.put("/auth/loginCode", "anon");
    }
}
