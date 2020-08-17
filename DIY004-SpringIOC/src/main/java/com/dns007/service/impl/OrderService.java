package com.dns007.service.impl;

import com.dns007.annotation.IocService;
import com.dns007.service.IOrderService;

/**
 * @author dns007
 * @version 1.0
 * @date 2020/8/17 8:05
 */
@IocService
public class OrderService implements IOrderService {
    @Override
    public String findOrder(String username) {
        return "用户"+username+"的订单编号是:0001";
    }
}
