package com.asugar.shorturl.controller;

import com.asugar.shorturl.common.ToResult;
import com.asugar.shorturl.service.UrlService;
import com.asugar.shorturl.util.HashUtils;
import com.asugar.shorturl.util.UrlCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description：Url控制器
 * @Auther： 一枚方糖
 * @Date： /08/13/20:18/
 */
@Controller
public class UrlController {
    @Autowired
    UrlService urlService;
    private static String host;


    @Value("${server.host}")
    public void setHost(String host){
        this.host = host;
    }

    @RequestMapping("/generate")
    @ResponseBody
    public ToResult generateShortUrl(@RequestParam("longURL") String longURL) {
        if(UrlCheck.checkURL(longURL)) {
            if(!longURL.startsWith("http")) {
                longURL = "http://" + longURL;
            }
            String ShortUrl = urlService.saveUrlMap(HashUtils.hashToBase62(longURL), longURL, longURL);
            return new ToResult(200,"生成成功",1,host+ShortUrl);
        }
        return new ToResult(500,"生成失败",1,"url格式错误");
    }

    @RequestMapping("/{shortURL}")
    public String redirect(@PathVariable String shortURL) {
        String longUrl = urlService.getLongUrl(shortURL);
        if(longUrl != null) {
            urlService.updateUrlViews(shortURL);
            // 302跳转
            return "redirect:" + longUrl;
        }
        // 短链接未生成
        return "redirect:/";
    }

    @RequestMapping("/getAllInfo")
    @ResponseBody
    public ToResult getAllInfo() {
        String allUrlInfo = urlService.getAllUrlInfo();
        return new ToResult(200,"查询所有Url",0,allUrlInfo);
    }


}
