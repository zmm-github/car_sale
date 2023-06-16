package com.zmm.car;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/car_sale?serverTimezone=UTC";
        String username = "root";
        String password = "20010101";
        String moduleName = "accountManage";
        String mapperLocation = "F:\\IdeaProjects\\car_sale\\src\\main\\resources\\mapper";
//        String tables = "accessory,car_model,car_series,customer,guide,lamp,lecture_hall,order_record,promotion,rescue_record,role,user,user_role";
        String tables = "orders";

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("zmm") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("F:\\IdeaProjects\\car_sale\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.zmm.car") // 设置父包名
//                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, mapperLocation)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生成的表名
                            ; // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}