package com.cyh.yuhanbakery;

import com.cyh.yuhanbakery.service.OssService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootTest
class YuhanBakeryApplicationTests {

    @Autowired
    private OssService ossService;

    @Test
    void uploadPresetImages() throws IOException {
        String baseDir = "C:\\Users\\Crayfish‘sLaptop\\.gemini\\antigravity\\brain\\2dfcefe8-5a21-45d7-8d3b-90c647d1e45d\\";
        Map<String, String> files = new LinkedHashMap<>();
        files.put("chocolate_cake", "chocolate_cake_1782378643800.png");
        files.put("fruit_cake", "fruit_cake_1782378659010.png");
        files.put("matcha_mousse", "matcha_mousse_1782378671465.png");
        files.put("cheese_cake", "cheese_cake_1782378683745.png");
        files.put("croissant_bread", "croissant_bread_1782378695781.png");
        files.put("toast_bread", "toast_bread_1782378709491.png");
        files.put("hot_latte", "hot_latte_1782378723835.png");
        files.put("iced_fruit_tea", "iced_fruit_tea_1782378737634.png");
        files.put("macarons_cookies", "macarons_cookies_1782378751591.png");
        files.put("afternoon_tea_combo", "afternoon_tea_combo_1782378765178.png");

        System.out.println("========== START UPLOADING IMAGES TO ALIYUN OSS ==========");
        for (Map.Entry<String, String> entry : files.entrySet()) {
            File localFile = new File(baseDir + entry.getValue());
            if (!localFile.exists()) {
                System.err.println("File not found: " + localFile.getAbsolutePath());
                continue;
            }
            try (FileInputStream fis = new FileInputStream(localFile)) {
                MultipartFile multipartFile = new MockMultipartFile(
                        "file",
                        entry.getValue(),
                        "image/png",
                        fis
                );
                String url = ossService.uploadFile(multipartFile);
                System.out.println("RESULT_URL:" + entry.getKey() + "=" + url);
            }
        }
        System.out.println("========== END UPLOADING IMAGES TO ALIYUN OSS ==========");
    }
}
