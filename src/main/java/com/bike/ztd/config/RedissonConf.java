package com.bike.ztd.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConf {


    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.database}")
    private Integer database;
    @Value("${spring.redis.timeOut}")
    private Integer timeOut;


    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        //single server
        SingleServerConfig singleServerConfig = config.useSingleServer();
        String schema = "redis://";
        singleServerConfig.setAddress(schema + host + ":" + port);
        if (StringUtils.isNotBlank(password)) {
            singleServerConfig.setPassword(password);
        }
        if (database != null) {
            singleServerConfig.setDatabase(database);
        }
        singleServerConfig.setConnectTimeout(timeOut);
        singleServerConfig.setConnectionMinimumIdleSize(10);
        return Redisson.create(config);

    }
}
