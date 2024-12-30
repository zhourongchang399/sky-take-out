package com.sky.service.impl;

import com.sky.properties.AliOssProperties;
import com.sky.service.UploadService;
import com.sky.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    AliOssUtil aliOssUtil;

    @Override
    public String upload(MultipartFile file) {
        // 随机生成唯一uuid作文件名
        String uuid = UUID.randomUUID().toString();
        // 获取文件类型
        String fileType = file.getOriginalFilename().split("\\.")[1];
        // 拼接文件名
        String newFileName = uuid + "." + fileType;
        // 调用alioss工具类上传
        try {
            return aliOssUtil.upload(file.getBytes(),newFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
