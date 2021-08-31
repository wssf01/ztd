package com.bike.ztd.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URL;

public interface OSSService {
    /**
     * 上传照片
     *
     * @param file
     * @return
     */
    String upload(MultipartFile file);

    /**
     * 删除照片
     *
     * @param bucketUrl
     */
    boolean delete(String bucketUrl);

    /**
     * 上传文件
     *
     * @param name
     * @param tempFile
     * @return
     */
    String upload(String name, File tempFile);


    /**
     * 获取下载文件地址
     *
     * @param ossPath
     * @return
     */
    String downLoad(String ossPath);
}
