package com.dns007.sqlsession;

import com.dns007.handler.UserMapperInvocationHandler;

import java.lang.reflect.Proxy;

/**
 * @author dns007
 * @version 1.0
 * @date 2020/8/18 8:22
 */
public class SqlSession {

    public static <T> T getUserMapper(Class clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz },
                new UserMapperInvocationHandler(clazz));
    }
}

/*
Mybatis执行流程：
读取配置文件io流，解析出我们要的信息，交给构建者，构建者使用工具类，构建了工厂对象，工厂的openSession方法提供了sqlSession对象，
sqlSession 调用 selectList 调用executor.query 调用doQuery 调用 preparedStatement excute()
 */