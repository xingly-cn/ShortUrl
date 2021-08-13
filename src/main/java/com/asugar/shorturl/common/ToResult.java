package com.asugar.shorturl.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description：统一结果封装
 * @Auther： 一枚方糖
 * @Date： /08/13/18:10/
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToResult {
    private Integer code;
    private String msg;
    private Integer count;
    private Object data;
}
