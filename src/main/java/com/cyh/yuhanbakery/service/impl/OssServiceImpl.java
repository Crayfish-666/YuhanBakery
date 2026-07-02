package com.cyh.yuhanbakery.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.cyh.yuhanbakery.common.BusinessException;
import com.cyh.yuhanbakery.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class OssServiceImpl implements OssService {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Value("${aliyun.oss.root-dir:}")
    private String rootDir;

    @Override
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + suffix;

        // 判断是否配置了真实的 AccessKey，如果没有则自动降级为本地文件夹存储
        if (isPlaceholderOrEmpty(accessKeyId) || isPlaceholderOrEmpty(accessKeySecret)) {
            log.warn("检测到阿里云 OSS 凭证未配置，自动降级为本地文件夹存储！");
            return saveLocally(file, fileName);
        }

        // 拼接对象存储键，实现将文件保存在指定根文件夹下
        String objectKey = fileName;
        if (rootDir != null && !rootDir.trim().isEmpty()) {
            String dir = rootDir.trim();
            if (!dir.endsWith("/")) {
                dir = dir + "/";
            }
            objectKey = dir + fileName;
        }

        // 执行阿里云 OSS 上传
        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            try (InputStream inputStream = file.getInputStream()) {
                ossClient.putObject(bucketName, objectKey, inputStream);
                // 拼接并返回阿里云 OSS 外部链接
                return "https://" + bucketName + "." + endpoint + "/" + objectKey;
            }
        } catch (Exception e) {
            log.error("阿里云 OSS 上传失败，尝试降级本地文件夹存储：", e);
            return saveLocally(file, fileName);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    private boolean isPlaceholderOrEmpty(String value) {
        return value == null || value.trim().isEmpty() || value.contains("your_access") || value.contains("placeholder");
    }

    private String saveLocally(MultipartFile file, String fileName) {
        try {
            // 获取项目根目录下的 uploads 文件夹并创建
            String userDir = System.getProperty("user.dir");
            Path uploadDir = Paths.get(userDir, "uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path targetPath = uploadDir.resolve(fileName);
            file.transferTo(targetPath.toFile());
            log.info("图片文件已成功保存至本地物理路径：{}", targetPath.toAbsolutePath());

            // 返回 localhost 的静态资源访问路径，配合 WebMvcConfig 访问
            return "http://127.0.0.1:8080/api/uploads/" + fileName;
        } catch (Exception e) {
            log.error("本地降级存储失败：", e);
            throw new BusinessException("图片上传发生错误");
        }
    }
}
