package com.bike.ztd.service.impl;

import com.aliyun.oss.OSS;
import com.bike.ztd.config.OSSConfig;
import com.bike.ztd.exception.BusinessException;
import com.bike.ztd.service.OSSService;
import com.bike.ztd.util.BeanUtils;
import com.bike.ztd.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import static com.bike.ztd.util.ErrorEnum.UPLOAD_ERROR;

@Slf4j
@Service
public class OSSServiceImpl implements OSSService {

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Override
    public String upload(MultipartFile file) {
        OSS ossClient = BeanUtils.getBean("ossClient");
        try {
            /**
             * 获取文件后缀名
             */
            String originalFilename = file.getOriginalFilename();
            int i = originalFilename.lastIndexOf(".");
            String png = originalFilename.substring(i);
            String fileName = UUIDUtils.systemUuid() + png;
            // 上传文件到指定的存储空间（bucketName）并将其保存为指定的文件名称（objectName）。
            ossClient.putObject(bucketName, fileName, file.getInputStream());
            log.info("oss upload success");
            return fileName;
        } catch (Exception e) {
            log.error("oss upload fail");
            e.printStackTrace();
            throw new BusinessException("UPLOAD_IMG_ERROR", "上传文件失败");
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public boolean delete(String bucketUrl) {
        OSS ossClient = BeanUtils.getBean("ossClient");
        try {
            // 删除Object.
            ossClient.deleteObject(bucketName, bucketUrl);
        } catch (Exception e) {
            log.error("oss delete fail");
            e.printStackTrace();
            return false;
        } finally {
            ossClient.shutdown();
        }
        log.info("oss delete success");
        return true;
    }

    @Override
    public String upload(String fileName, File tempFile) {
        OSS ossClient = BeanUtils.getBean("ossClient");
        try {
            // 上传文件到指定的存储空间（bucketName）并将其保存为指定的文件名称（objectName）。
            InputStream in = new FileInputStream(tempFile);
            ossClient.putObject(bucketName, fileName, in);
            log.info("oss upload success");
            return fileName;
        } catch (Exception e) {
            log.error("oss upload fail");
            e.printStackTrace();
            throw new BusinessException("UPLOAD_IMG_ERROR", e.getMessage());
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public String downLoad(String ossPath) {
        OSS ossClient = BeanUtils.getBean("ossClient");
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 1);
        return ossClient.generatePresignedUrl(bucketName, ossPath, expiration).toString();
    }
}
