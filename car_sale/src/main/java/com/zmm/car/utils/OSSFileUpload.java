package com.zmm.car.utils;/**
 * @author 阿毛
 * @date 2023/6/14 14:15
 */

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.zmm.car.common.vo.ResultMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @program: car_sale
 * @description:
 * @author: Try harder
 * @create: 2023-06-14 14:15
 * @version:1.0
 **/
@RestController
@RequestMapping("/file")
public class OSSFileUpload {
    // 阿里域名
    public static final String ALI_DOMAIN = "https://car-sale-web.oss-cn-beijing.aliyuncs.com/";

    @PostMapping("/ossUpload")
    public static ResultMap uploadImage(MultipartFile file)  {
        // 生成一个新的文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + suffix;

        // 上传路径
        String path = "images/" + fileName;

        // 地域节点
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        String accessKeyId = "LTAI5tCs4mb8rC7zeQSCarbU";
        String accessKeySecret = "dqtdA8Lc04UUOe6R3yQVw4iJRjqC6Y";

        // OSS客户端对象
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.putObject(
                    "car-sale-web",  // 仓库名
                    path,  // 上传路径
                    file.getInputStream()
            );
            ossClient.shutdown();
            return ResultMap.success("上传成功", ALI_DOMAIN + path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResultMap.fail("上传失败");
    }
}