package com.zmm.car.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultMap<T> {

    private Integer code;
    private String message;
    private T data;

    public static <T> ResultMap<T> success(){
        return new ResultMap<>(20000, "success", null);
    }

    /* 以下对success方法进行重载 */
    public static <T> ResultMap<T> success(T data){
        return new ResultMap<>(20000, "success", data);
    }

    public static <T> ResultMap<T> success(String message, T data){
        return new ResultMap<>(20000, message, data);
    }

    public static <T> ResultMap<T> success(String message){
        return new ResultMap<>(20000, message, null);
    }

    public static <T> ResultMap<T> fail(){
        return new ResultMap<>(20001, "fail", null);
    }

    /* 以下对fail方法进行重载 */
    public static <T> ResultMap<T> fail(String message){
        return new ResultMap<>(20001, message, null);
    }

    public static <T> ResultMap<T> fail(Integer code, String message){
        return new ResultMap<>(code, message, null);
    }

    public static <T> ResultMap<T> fail(Integer code){
        return new ResultMap<>(code, "fail", null);
    }
}