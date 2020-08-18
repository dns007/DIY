package com.dns007.test;

import com.dns007.bean.User;
import com.dns007.mapper.UserMapper;
import com.dns007.sqlsession.SqlSession;

import java.sql.SQLException;

/**
 * @author dns007
 * @version 1.0
 * @date 2020/8/17 8:19
 */
public class MybatisTest {
    public static void main(String[] args) throws SQLException {
        UserMapper userMapper = SqlSession.getUserMapper(UserMapper.class);
        User user = userMapper.selectUser("dns007", "wuhan");
        //int row = userMapper.insertUser("dns007", "wuhan");
        System.out.println("row:" + user);
    }
}