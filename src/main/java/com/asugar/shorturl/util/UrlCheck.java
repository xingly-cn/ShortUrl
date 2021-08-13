package com.asugar.shorturl.util;

import java.util.regex.Pattern;

/**
 * @Description：Url校验
 * @Auther： 一枚方糖
 * @Date： /08/13/19:35/
 */
public class UrlCheck {
    private static final Pattern URL_REG = Pattern.compile("^(((ht|f)tps?):\\/\\/)?[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?$");
    public static boolean checkURL(String url) {
        return URL_REG.matcher(url).matches();
    }
}
