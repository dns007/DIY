package com.dns007.service.impl;

import com.dns007.annotation.IocResource;
import com.dns007.annotation.IocService;
import com.dns007.service.IOrderService;
import com.dns007.service.IUserService;

/**
 * @author dns007
 * @version 1.0
 * @date 2020/8/17 8:05
 */
@IocService(name = "userbiz")
public class UserService implements IUserService {

    /*比较脆弱啊 这块的属性名称一定要用实现类来命名 且 按照第一个字母要小写的原则 否则很报错的*/
    @IocResource
    private IOrderService orderService;
    @Override
    public String find(String name) {
        return orderService.findOrder(name);
    }
}
