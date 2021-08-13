package com.asugar.shorturl.mapper;

import com.asugar.shorturl.common.UrlInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @Description：Url持久层
 * @Auther： 一枚方糖
 * @Date： /08/13/19:04/
 */
@Mapper
@Repository
public interface UrlMapper {
    /**
     * 查询短网址
     * @param url
     * @return
     */
    @Select("select lurl from url_map where surl = #{surl}")
    String getLongUrl(String url);

    /**
     * 保存短网址
     * @param urlInfo
     * @return
     */
    @Insert("insert into url_map (surl, lurl, views, create_time) values (#{surl}, #{lurl}, #{views}, #{createTime})")
    int saveUrlInfo(UrlInfo urlInfo);

    /**
     * 更新网址访问次数
     * @param surl
     * @return
     */
    @Update("update url_map set views=views+1 where surl = #{surl}")
    int updataUrlViews(String surl);

    /**
     * 查询所有Url信息
     * @return
     */
    @Select("select * from url_map")
    Object getAllUrlInfo();

}
