package com.bike.ztd.service.impl;

import com.bike.ztd.service.ExportService;
import com.bike.ztd.service.TWaybillService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class ExportServiceImpl implements ExportService {

    @Resource
    private TWaybillService waybillService;

    @Async
    @Override
    public void listExport(String key, Map<String, Object> map) {
        waybillService.listExport(key, map);
    }
}
