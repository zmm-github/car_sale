package com.zmm.car;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.zmm.car.mapper")
public class CarSaleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarSaleApplication.class, args);
    }

}
