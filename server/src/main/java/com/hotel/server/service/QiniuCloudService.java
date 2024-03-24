package com.hotel.server.service;


import org.springframework.web.multipart.MultipartFile;

/**
 * 七牛云上传文件服务类
 */
public interface QiniuCloudService {
    /**
     * 字节数组上传
     */
    String bytesUpload(byte[] fileBytes, String fileName);

    /**
     * 上传文件
     */
    String fileUpload(MultipartFile file, String fileName);
}
