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
    AliOssProperties aliOssProperties;

    @Override
    public String upload(MultipartFile file) {
        // 获取配置参数类中的数值并赋值给alioss工具类
        AliOssUtil aliOssUtil = new AliOssUtil(
                aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
        // 随机生成唯一uuid作文件名
        String uuid = UUID.randomUUID().toString();
        // 获取文件类型
        String fileType = file.getOriginalFilename().split("\\.")[1];
        // 拼接文件名
        String newFileName = uuid + "." + fileType;
        // 调用alioss工具类上传
        try {
            aliOssUtil.upload(file.getBytes(),newFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        https://just-take-out.oss-cn-nanjing.aliyuncs.com/755cbfae-3e12-48ff-bed6-c0993f8de4e2.jpg
        // 生成alioss中的绝对url
        String url = aliOssProperties.getEndpoint().split("//")[0] +
                aliOssProperties.getBucketName() +
                "." +
                aliOssProperties.getEndpoint().split("//")[1] +
                "/" +
                newFileName;
        return url;
    }

}
