package com.asugar.shorturl.controller;


import com.asugar.shorturl.mapper.UrlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description：首页控制器
 * @Auther： 一枚方糖
 * @Date： /08/13/19:12/
 */
@Controller
public class IndexController {

    @RequestMapping({"/","/index","/index.html"})
    public String index(){
        return "index";
    }

}
