package com.zmm.car.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Joey
 * @Description: 全局格式化 后台<-->前端 的LocalDateTime、LocalDate时间格式化
 *               如果不想使用全局格式，则在bean中使用@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
 *               和@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")注解
 * @date 2021-03-12 15:09:05
 */
@Configuration
public class MyDateConfig {

    /**
     * 前端传入时间格式
     */
//    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    @Value("${spring.jackson.date-format}")
    private String jsonPattern;

    /**
     * localDateTime 序列化器,后台返回给前端时间格式化
     * @return
     */
    @Bean
    public LocalDateTimeSerializer localDateTimeSerializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(jsonPattern));
    }

    /**
     * localDateTime 反序列化器，前端传给后台时间格式化
     * @return
     */
    @Bean
    public LocalDateTimeDeserializer localDateTimeDeserializer() {
        return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(jsonPattern));
    }

    /**
     *
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.serializerByType(LocalDateTime.class, localDateTimeSerializer());
            builder.deserializerByType(LocalDateTime.class, localDateTimeDeserializer());
        };
    }


}