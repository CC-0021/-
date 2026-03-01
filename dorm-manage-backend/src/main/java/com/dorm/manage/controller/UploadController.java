package com.dorm.manage.controller;

import com.dorm.manage.common.Result;
import com.dorm.manage.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 *
 * @author dorm-manage
 */
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    private final FileUploadUtil fileUploadUtil;

    @PostMapping("/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file,
                                      @RequestParam(defaultValue = "repair") String dir) {
        String url = fileUploadUtil.save(file, dir);
        return Result.ok(url);
    }
}
