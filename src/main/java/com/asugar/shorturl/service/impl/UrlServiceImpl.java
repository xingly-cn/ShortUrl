package com.asugar.shorturl.service.impl;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.bloomfilter.BloomFilterUtil;
import cn.hutool.json.JSONUtil;
import com.asugar.shorturl.common.UrlInfo;
import com.asugar.shorturl.mapper.UrlMapper;
import com.asugar.shorturl.service.UrlService;
import com.asugar.shorturl.util.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description：Url服务层实现
 * @Auther： 一枚方糖
 * @Date： /08/13/19:29/
 */
@Service
public class UrlServiceImpl implements UrlService {
    // 接口注入
    @Autowired
    UrlMapper urlMapper;
    @Autowired
    StringRedisTemplate redisTemplate;

    // 自定义长链接 - 防止重复字符串
    private static final String duplicate = "*";
    // 最近使用短连接过期时间（10分钟）
    private static final long timeout = 10;
    // bool过滤器
    private static final BitMapBloomFilter filter = BloomFilterUtil.createBitMap(10);

    @Override
    public String getLongUrl(String shortURL) {
        // 查找Redis中是否有缓存
        String longUrl = redisTemplate.opsForValue().get(shortURL);
        if (longUrl != null) {
            // 有缓存,增加缓存时间
            redisTemplate.expire(shortURL,timeout, TimeUnit.MINUTES);
            return longUrl;
        }
        // Redis没有缓存,查数据库
        longUrl = urlMapper.getLongUrl(shortURL);
        if(longUrl != null) {
            // 将短链接添加缓存
            redisTemplate.opsForValue().set(shortURL,longUrl,timeout,TimeUnit.MINUTES);
        }
        return longUrl;
    }

    @Override
    public String saveUrlMap(String shortURL, String longURL, String originalURL) {
        // 保存长度为1的短链接
        if(shortURL.length() == 1) {
            longURL += duplicate;
            shortURL = saveUrlMap(HashUtils.hashToBase62(longURL),longURL,originalURL);
        }
        // bool过滤器查找是否存在
        else if(filter.contains(shortURL)) {
            // 存在,查Redis是否有缓存
            String redisLongUrl = redisTemplate.opsForValue().get(shortURL);
            if(redisLongUrl != null && originalURL.equals(redisLongUrl)) {
                // 更新过期时间
                redisTemplate.expire(shortURL,timeout,TimeUnit.MINUTES);
                return shortURL;
            }
            // 没有缓存,长链接后加上指定字符串,重新hash
            longURL += duplicate;
            shortURL = saveUrlMap(HashUtils.hashToBase62(longURL),longURL,originalURL);
        } else {
            // 不存在,存入数据库
            try {
                UrlInfo urlInfo = new UrlInfo();
                urlInfo.setSurl(shortURL);
                urlInfo.setLurl(originalURL);
                urlInfo.setViews(0);
                urlInfo.setCreateTime(new Date());
                urlMapper.saveUrlInfo(urlInfo);
                filter.add(shortURL);
                // 添加缓存
                redisTemplate.opsForValue().set(shortURL,originalURL,timeout,TimeUnit.MINUTES);
            } catch (Exception e) {
                if(e instanceof DuplicateKeyException) {
                    // 数据库已经存在此短链接，则可能是布隆过滤器误判，在长链接后加上指定字符串，重新hash
                    longURL += duplicate;
                    shortURL = saveUrlMap(HashUtils.hashToBase62(longURL), longURL, originalURL);
                } else {
                    throw e;
                }
            }
        }
        return shortURL;
    }

    @Override
    public String getAllUrlInfo() {
        List<UrlInfo> allUrlInfo = urlMapper.getAllUrlInfo();
        String allInfo = JSONUtil.toJsonStr(allUrlInfo);
        return allInfo;
    }

    @Override
    public void updateUrlViews(String shortURL) {
        urlMapper.updataUrlViews(shortURL);
    }
}
