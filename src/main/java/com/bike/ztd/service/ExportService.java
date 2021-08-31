package com.bike.ztd.service;

import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Map;

@EnableAsync
public interface ExportService {

    /**
     * 异步导出
     *
     * @param key
     * @param map
     */
    void listExport(String key, Map<String, Object> map);
}
