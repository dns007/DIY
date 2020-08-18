package com.dns007.handler;

import com.dns007.annotation.MyInsert;
import com.dns007.annotation.MyParam;
import com.dns007.annotation.MyQuery;
import com.dns007.util.JDBCUtil;
import com.dns007.util.SQLUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dns007
 * @version 1.0
 * @date 2020/8/18 8:07
 */
public class UserMapperInvocationHandler implements InvocationHandler {

    private Class userMapperClazz;

    public UserMapperInvocationHandler(Class userMapperClazz) {
        this.userMapperClazz = userMapperClazz;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MyQuery myQuery =  method.getDeclaredAnnotation(MyQuery.class);
        //查询
        if (null != myQuery) {
            //获取结果
            return getResult(method, myQuery, args);
        }
        //插入
        MyInsert myInsert = method.getDeclaredAnnotation(MyInsert.class);
        if (null != myInsert) {
            String insertSql = myInsert.value();
            //插入参数
            String[] insertParam = SQLUtil.getInsertParams(insertSql);
            //参数绑定
            ConcurrentHashMap<String, Object> paramMap = getMethodParam(method, args);
            //将参数值加入list
            List<Object> paramValueList = addParamToList(insertParam, paramMap);

            insertSql = SQLUtil.replaceParam(insertSql, insertParam);
            return JDBCUtil.insert(insertSql, false, paramValueList);
        }
        return null;
    }


    private Object getResult(Method method, MyQuery myQuery, Object[] args) throws SQLException, IllegalAccessException, InstantiationException {
        String querySql = myQuery.value();
        //获取sql参数
        List<Object> paramList = SQLUtil.getSelectParams(querySql);
        //替换sql参数
        querySql = SQLUtil.replaceParam(querySql, paramList);
        //获取方法参数，绑定值。
        ConcurrentHashMap<String, Object> paramMap = getMethodParam(method, args);

        List<Object> paramValueList = new ArrayList<>();
        //将参数值装入list集合
        for (Object param : paramList) {
            Object paramValue = paramMap.get(param);
            paramValueList.add(paramValue);
        }

        System.out.println("paramValueList:" + paramValueList);

        ResultSet rs = JDBCUtil.query(querySql, paramValueList);
        if (!rs.next()) {return null;}

        Class<?> returnTypeClazz = method.getReturnType();
        Object obj = returnTypeClazz.newInstance();
        //光标往前移动一位
        rs.previous();
        while (rs.next()) {
            Field[] fields = returnTypeClazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                String fieldValue = rs.getString(fieldName);
                field.setAccessible(true);
                field.set(obj, fieldValue);
            }
        }
        return obj;
    }

    private ConcurrentHashMap getMethodParam(Method method, Object[] args) {
        ConcurrentHashMap paramMap = new ConcurrentHashMap();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            MyParam gdzParam = parameters[i].getAnnotation(MyParam.class);
            if (null == gdzParam) {continue;}
            String paramName = gdzParam.value();
            Object paramValue = args[i];
            paramMap.put(paramName, paramValue);
        }
        return paramMap;
    }


    private List<Object> addParamToList(String[] insertParam, ConcurrentHashMap<String, Object> paramMap) {
        List<Object> paramValueList = new ArrayList<>();
        for (String param : insertParam) {
            Object paramValue = paramMap.get(param.trim());
            paramValueList.add(paramValue);
        }
        return paramValueList;
    }

}