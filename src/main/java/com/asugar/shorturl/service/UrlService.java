package com.asugar.shorturl.service;

import org.springframework.scheduling.annotation.Async;

/**
 * @Description：Url服务层
 * @Auther： 一枚方糖
 * @Date： /08/13/19:26/
 */
public interface UrlService {

    String getLongUrl(String shortURL);

    String saveUrlMap(String shortURL, String longURL, String originalURL);

    String getAllUrlInfo();

    @Async
    void updateUrlViews(String shortURL);
}
