package com.dns007.test;

import com.dns007.context.SpringDIYContext;
import com.dns007.service.IUserService;

/**
 * @author dns007
 * @version 1.0
 * @date 2020/8/17 8:19
 */
public class SpringIOCTest {
    public static void main(String[] args)throws Exception {
        String path = "com.dns007.service.impl";
        SpringDIYContext context = new SpringDIYContext(path);
        IUserService userService = (IUserService) context.getBean("userbiz");
        System.out.println(userService.find("dns007"));
    }
}