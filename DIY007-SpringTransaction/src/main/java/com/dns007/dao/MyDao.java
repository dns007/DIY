package com.dns007.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author dns007
 * @version 1.0
 * @date 2020/8/21 8:08
 */

@Repository
public class MyDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void add(String name, String city) {
        String sql = "insert into user(username, city) values(?, ?)";
        int result = jdbcTemplate.update(sql, name, city);
        System.out.println("执行结果：" + result);
    }

}
