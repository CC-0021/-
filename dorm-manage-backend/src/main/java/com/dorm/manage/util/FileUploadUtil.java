package com.dorm.manage.util;

import com.dorm.manage.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 文件上传工具 - 限制格式 jpg/png，单张≤2MB
 *
 * @author dorm-manage
 */
@Component
public class FileUploadUtil {

    @Value("${dorm.upload.path:./uploads}")
    private String uploadPath;

    private static final List<String> ALLOWED_EXT = Arrays.asList("jpg", "jpeg", "png");
    private static final long MAX_SIZE = 2 * 1024 * 1024;

    public String save(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }
        String ext = getExtension(file.getOriginalFilename());
        if (!ALLOWED_EXT.contains(ext.toLowerCase())) {
            throw new BusinessException("仅支持 jpg/png 格式");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new BusinessException("单张图片不能超过2MB");
        }
        try {
            Path dir = Paths.get(uploadPath, subDir);
            Files.createDirectories(dir);
            String fileName = UUID.randomUUID().toString().replace("-", "") + "." + ext;
            Path target = dir.resolve(fileName);
            file.transferTo(target.toFile());
            return "/uploads/" + subDir + "/" + fileName;
        } catch (IOException e) {
            throw new BusinessException("上传失败");
        }
    }

    private String getExtension(String name) {
        if (name == null || !name.contains(".")) {
            return "";
        }
        return name.substring(name.lastIndexOf('.') + 1);
    }
}
