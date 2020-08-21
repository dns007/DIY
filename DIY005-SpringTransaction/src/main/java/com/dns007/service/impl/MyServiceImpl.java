package com.dns007.service.impl;

import com.dns007.annotation.MyTransactional;
import com.dns007.dao.MyDao;
import com.dns007.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dns007
 * @version 1.0
 * @date 2020/8/21 8:11
 */
@Service
public class MyServiceImpl implements MyService {

    @Autowired
    MyDao myDao;

    @Override
    @MyTransactional
    public void addUser() {
        // 先执行第一条语句
        myDao.add("dns007", "武汉");

        // 添加一条错误语句
//        int index = 1 / 0;

        // 在执行第二条语句
        myDao.add("dns008", "深圳");

    }
}
