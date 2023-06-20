package com.zmm.car.utils;/**
 * @author 阿毛
 * @date 2023/6/10 17:42
 */

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @program: car_sale
 * @description:
 * @author: Try harder
 * @create: 2023-06-10 17:42
 * @version:1.0
 **/
@RestController
@RequestMapping("/tinymce")
public class TinymceUpload {

    @PostMapping("/upload")
    public Map<String, Object> uploadFile(@RequestParam(value = "file", required = true) MultipartFile file){
        HashMap<String, Object> map = new HashMap<>();
        if (file.getSize() > 0 && file != null) {
            // 获取传过来的文件名字
            String originalFilename = file.getOriginalFilename();
            // 为了防止重名覆盖，获取随机UUID+原始文件的后缀名
            String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            // 设置保存地址
            // 1.后台保存位置
            String path = "F:\\VScode_workspase\\car_sale_web\\src\\assets\\upload\\tinymce\\";
            File newFile = new File(path + fileName);
            // 判断文件是否存在
            if (!newFile.getParentFile().exists()){
                // 不存在就创建一个
                newFile.getParentFile().mkdirs();
            }
            try{
                // 后台上传
                file.transferTo(newFile);
                String location = "/upload/tinymce" + fileName;
                map.put("location", location);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}