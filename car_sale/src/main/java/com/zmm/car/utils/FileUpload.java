package com.zmm.car.utils;

import com.zmm.car.common.vo.ResultMap;
import com.zmm.car.entity.CarModel;
import com.zmm.car.service.ICarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileUpload {

    @Value("${img.path}")
    private String path;

    @PostMapping("/upload")
    public ResultMap uploadFile(@RequestParam(value = "file", required = true) MultipartFile file){
        // 判断文件是否为空
        if (file.isEmpty()) {
            return ResultMap.fail("未上传任何文件");
        }

        // 获取传过来的文件名字
        String originalFilename = file.getOriginalFilename();
        // 为了防止重名覆盖，获取系统时间戳+原始文件的后缀名
        String fileName = System.currentTimeMillis() + "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        // 设置保存地址
        // 1.后台保存位置
//        String path = "C:/Users/86159/Desktop/img/";
        File newFile = new File(path + fileName);
        // 判断文件是否存在
        if (!newFile.getParentFile().exists()){
            // 不存在就创建一个
            newFile.getParentFile().mkdirs();
        }

        try{
            // 后台上传
            file.transferTo(newFile);
            return ResultMap.success("文件上传成功", fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultMap.fail();
        }
    }
}