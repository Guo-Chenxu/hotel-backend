package com.hotel.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.hotel.server.service.QiniuCloudService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;


@Slf4j
@Service
public class QiniuCloudServiceImpl implements QiniuCloudService {

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucketName}")
    private String bucketName;

    @Value("${qiniu.domain}")
    private String domain;

    private Auth auth;

    @PostConstruct
    private void init() {
        log.info("accessKey: {}", accessKey);
        log.info("secretKey: {}", secretKey);
        auth = Auth.create(accessKey, secretKey);
    }

    private String getUpToken() {
        return auth.uploadToken(bucketName);
    }


    @Override
    public String bytesUpload(byte[] fileBytes, String fileName) {
        Configuration cfg = new Configuration(Region.huabei());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        UploadManager uploadManager = new UploadManager(cfg);

        try {
            Response response = uploadManager.put(fileBytes, fileName, getUpToken());
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            log.info("upload success: {}", putRet);
            return domain + "/" + putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.info("QiniuException: {}", r.toString());
            throw new RuntimeException("文件上传失败");
        }
    }

    @Override
    public String fileUpload(MultipartFile file, String fileName) {
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            log.error("文件上传失败，{}", e.getMessage());
            throw new RuntimeException("文件上传失败");
        }
        return this.bytesUpload(bytes, fileName);
    }
}
