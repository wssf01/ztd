package com.bike.ztd.service;

import com.aliyuncs.exceptions.ClientException;

public interface SendMessageService {
    /**
     * 发送验证码
     */
    void sendCode(String telephone, String code) throws ClientException;
}
