package com.bike.ztd.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * Created by zkf5485 on 2018/5/29.
 */
@Component
@Slf4j
public class KeyExpireMessageListener implements MessageListener {

    public final static String LISTENER_PATTERN = "__keyevent@0__:expired";

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getBody());
        log.info("Token: " + key + " is expired.");
    }

}
