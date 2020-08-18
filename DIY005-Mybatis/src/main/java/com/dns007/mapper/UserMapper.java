package com.dns007.mapper;

import com.dns007.annotation.MyInsert;
import com.dns007.annotation.MyParam;
import com.dns007.annotation.MyQuery;
import com.dns007.bean.User;

/**
 * @author dns007
 * @version 1.0
 * @date 2020/8/18 8:19
 */
public interface UserMapper {

    @MyQuery("select username, city from user  where username = #{username} and city = #{city}")
    User selectUser(@MyParam("username") String name, @MyParam("city") String city);


    @MyInsert("insert into user(username, city) values(#{username}, #{city})")
    int insertUser(@MyParam("username") String name, @MyParam("city") String city);


}