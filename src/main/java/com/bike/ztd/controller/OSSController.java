package com.bike.ztd.controller;

import com.bike.ztd.service.OSSService;
import com.bike.ztd.util.ResponseEntity;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequestMapping("oss")
public class OSSController {

    @Autowired
    private OSSService ossService;


    @PostMapping("upload")
    @ApiOperation(value = "上传图片,返回oss中的文件名", response = String.class)
    public ResponseEntity uploadImg(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.success(ossService.upload(file));
    }

    @GetMapping("downLoad")
    @ApiOperation(value = "返回下载文件地址，直接输入浏览器就能下载相应文件名的文件", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "oss中存在的文件名")
    })
    public ResponseEntity downLoad(@RequestParam("fileName") String fileName) {
        return ResponseEntity.success(ossService.downLoad(fileName));
    }
}
