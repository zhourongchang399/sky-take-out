package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2024/12/30 15:41
 */
@RestController
@RequestMapping("/admin/common")
@Slf4j
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("上传文件:{}", file.getOriginalFilename());
        String url = uploadService.upload(file);
        return Result.success(url);
    }

}
