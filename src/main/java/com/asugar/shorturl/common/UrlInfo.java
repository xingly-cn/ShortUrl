package com.asugar.shorturl.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description：Url实体类
 * @Auther： 一枚方糖
 * @Date： /08/13/18:14/
 */
@Data
@NoArgsConstructor
public class UrlInfo {
    private Long id;
    private String surl;
    private String lurl;
    private Integer views;
    private Date createTime;
}
