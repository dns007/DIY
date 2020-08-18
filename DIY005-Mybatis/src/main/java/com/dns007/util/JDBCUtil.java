package com.dns007.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
/**
 * @author dns007
 * @version 1.0
 * @date 2020/8/18 8:23
 */
public final class JDBCUtil {

    private static String connect;
    private static String driverClassName;
    private static String URL;
    private static String username;
    private static String password;
    private static boolean autoCommit;

    /** 声明一个 Connection类型的静态属性，用来缓存一个已经存在的连接对象 */
    private static Connection conn;

    static {
        config();
    }

    /**
     * 开头配置自己的数据库信息
     */
    private static void config() {
        /*
         * 获取驱动
         */
        driverClassName = "com.mysql.jdbc.Driver";
        /*
         * 获取URL
         */
        URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8";
        /*
         * 获取用户名
         */
        username = "root";
        /*
         * 获取密码
         */
        password = "123456";
        /*
         * 设置是否自动提交，一般为false不用改
         */
        autoCommit = false;

    }

    /**
     * 载入数据库驱动类
     */
    private static boolean load() {
        try {
            Class.forName(driverClassName);
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("驱动类 " + driverClassName + " 加载失败");
        }

        return false;
    }

    /**
     * 专门检查缓存的连接是否不可以被使用 ，不可以被使用的话，就返回 true
     */
    private static boolean invalid() {
        if (conn != null) {
            try {
                if (conn.isClosed() || !conn.isValid(3)) {
                    return true;
                    /*
                     * isValid方法是判断Connection是否有效,如果连接尚未关闭并且仍然有效，则返回 true
                     */
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            /*
             * conn 既不是 null 且也没有关闭 ，且 isValid 返回 true，说明是可以使用的 ( 返回 false )
             */
            return false;
        } else {
            return true;
        }
    }

    /**
     * 建立数据库连接
     */
    public static Connection connect() {
        if (invalid()) { /* invalid为true时，说明连接是失败的 */
            /* 加载驱动 */
            load();
            try {
                /* 建立连接 */
                conn = DriverManager.getConnection(URL, username, password);
            } catch (SQLException e) {
                System.out.println("建立 " + connect + " 数据库连接失败 , " + e.getMessage());
            }
        }
        return conn;
    }

    /**
     * 设置是否自动提交事务
     **/
    public static void transaction() {

        try {
            conn.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            System.out.println("设置事务的提交方式为 : " + (autoCommit ? "自动提交" : "手动提交") + " 时失败: " + e.getMessage());
        }

    }

    /**
     * 创建 Statement 对象
     */
    public static Statement statement() {
        Statement st = null;
        connect();
        /* 如果连接是无效的就重新连接 */
        transaction();
        /* 设置事务的提交方式 */
        try {
            st = conn.createStatement();
        } catch (SQLException e) {
            System.out.println("创建 Statement 对象失败: " + e.getMessage());
        }

        return st;
    }

    /**
     * 根据给定的带参数占位符的SQL语句，创建 PreparedStatement 对象
     *
     * @param SQL
     *            带参数占位符的SQL语句
     * @return 返回相应的 PreparedStatement 对象
     */
    private static PreparedStatement prepare(String SQL, boolean autoGeneratedKeys) {

        PreparedStatement ps = null;
        connect();
        /* 如果连接是无效的就重新连接 */
        transaction();
        /* 设置事务的提交方式 */
        try {
            if (autoGeneratedKeys) {
                ps = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            } else {
                ps = conn.prepareStatement(SQL);
            }
        } catch (SQLException e) {
            System.out.println("创建 PreparedStatement 对象失败: " + e.getMessage());
        }

        return ps;

    }

    public static ResultSet query(String SQL, List<Object> params) {

        if (SQL == null || SQL.trim().isEmpty() || !SQL.trim().toLowerCase().startsWith("select")) {
            throw new RuntimeException("你的SQL语句为空或不是查询语句");
        }
        ResultSet rs = null;
        if (params != null && params.size() > 0) {
            /* 说明 有参数 传入，就需要处理参数 */
            PreparedStatement ps = prepare(SQL, false);
            try {
                for (int i = 0; i < params.size(); i++) {
                    ps.setObject(i + 1, params.get(i));
                }
                rs = ps.executeQuery();
            } catch (SQLException e) {
                System.out.println("执行SQL失败: " + e.getMessage());
            }
        } else {
            /* 说明没有传入任何参数 */
            Statement st = statement();
            try {
                rs = st.executeQuery(SQL); // 直接执行不带参数的 SQL 语句
            } catch (SQLException e) {
                System.out.println("执行SQL失败: " + e.getMessage());
            }
        }

        return rs;

    }

    private static Object typeof(Object o) {
        Object r = o;

        if (o instanceof java.sql.Timestamp) {
            return r;
        }
        // 将 java.util.Date 转成 java.sql.Date
        if (o instanceof java.util.Date) {
            java.util.Date d = (java.util.Date) o;
            r = new java.sql.Date(d.getTime());
            return r;
        }
        // 将 Character 或 char 变成 String
        if (o instanceof Character || o.getClass() == char.class) {
            r = String.valueOf(o);
            return r;
        }
        return r;
    }

    public static boolean execute(String SQL, Object... params) {
        if (SQL == null || SQL.trim().isEmpty() || SQL.trim().toLowerCase().startsWith("select")) {
            throw new RuntimeException("你的SQL语句为空或有错");
        }
        boolean r = false;
        /* 表示 执行 DDL 或 DML 操作是否成功的一个标识变量 */

        /* 获得 被执行的 SQL 语句的 前缀 */
        SQL = SQL.trim();
        SQL = SQL.toLowerCase();
        String prefix = SQL.substring(0, SQL.indexOf(" "));
        String operation = ""; // 用来保存操作类型的 变量
        // 根据前缀 确定操作
        switch (prefix) {
            case "create":
                operation = "create table";
                break;
            case "alter":
                operation = "update table";
                break;
            case "drop":
                operation = "drop table";
                break;
            case "truncate":
                operation = "truncate table";
                break;
            case "insert":
                operation = "insert :";
                break;
            case "update":
                operation = "update :";
                break;
            case "delete":
                operation = "delete :";
                break;
        }
        if (params.length > 0) { // 说明有参数
            PreparedStatement ps = prepare(SQL, false);
            Connection c = null;
            try {
                c = ps.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                for (int i = 0; i < params.length; i++) {
                    Object p = params[i];
                    p = typeof(p);
                    ps.setObject(i + 1, p);
                }
                ps.executeUpdate();
                commit(c);
                r = true;
            } catch (SQLException e) {
                System.out.println(operation + " 失败: " + e.getMessage());
                rollback(c);
            }

        } else { // 说明没有参数

            Statement st = statement();
            Connection c = null;
            try {
                c = st.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // 执行 DDL 或 DML 语句，并返回执行结果
            try {
                st.executeUpdate(SQL);
                commit(c); // 提交事务
                r = true;
            } catch (SQLException e) {
                System.out.println(operation + " 失败: " + e.getMessage());
                rollback(c); // 回滚事务
            }
        }
        return r;
    }

    /*
     *
     * @param SQL 需要执行的 INSERT 语句
     *
     * @param autoGeneratedKeys 指示是否需要返回由数据库产生的键
     *
     * @param params 将要执行的SQL语句中包含的参数占位符的 参数值
     *
     * @return 如果指定 autoGeneratedKeys 为 true 则返回由数据库产生的键； 如果指定 autoGeneratedKeys
     * 为 false 则返回受当前SQL影响的记录数目
     */
    public static int insert(String SQL, boolean autoGeneratedKeys, List<Object> params) {
        int var = -1;
        if (SQL == null || SQL.trim().isEmpty()) {
            throw new RuntimeException("你没有指定SQL语句，请检查是否指定了需要执行的SQL语句");
        }
        // 如果不是 insert 开头开头的语句
        if (!SQL.trim().toLowerCase().startsWith("insert")) {
            System.out.println(SQL.toLowerCase());
            throw new RuntimeException("你指定的SQL语句不是插入语句，请检查你的SQL语句");
        }
        // 获得 被执行的 SQL 语句的 前缀 ( 第一个单词 )
        SQL = SQL.trim();
        SQL = SQL.toLowerCase();
        if (params.size() > 0) { // 说明有参数
            PreparedStatement ps = prepare(SQL, autoGeneratedKeys);
            Connection c = null;
            try {
                c = ps.getConnection(); // 从 PreparedStatement 对象中获得 它对应的连接对象
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                for (int i = 0; i < params.size(); i++) {
                    Object p = params.get(i);
                    p = typeof(p);
                    ps.setObject(i + 1, p);
                }
                int count = ps.executeUpdate();
                if (autoGeneratedKeys) { // 如果希望获得数据库产生的键
                    ResultSet rs = ps.getGeneratedKeys(); // 获得数据库产生的键集
                    if (rs.next()) { // 因为是保存的是单条记录，因此至多返回一个键
                        var = rs.getInt(1); // 获得值并赋值给 var 变量
                    }
                } else {
                    var = count; // 如果不需要获得，则将受SQL影像的记录数赋值给 var 变量
                }
                commit(c);
            } catch (SQLException e) {
                System.out.println("数据保存失败: " + e.getMessage());
                rollback(c);
            }
        } else { // 说明没有参数
            Statement st = statement();
            Connection c = null;
            try {
                c = st.getConnection(); // 从 Statement 对象中获得 它对应的连接对象
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // 执行 DDL 或 DML 语句，并返回执行结果
            try {
                int count = st.executeUpdate(SQL);
                if (autoGeneratedKeys) { // 如果企望获得数据库产生的键
                    ResultSet rs = st.getGeneratedKeys(); // 获得数据库产生的键集
                    if (rs.next()) { // 因为是保存的是单条记录，因此至多返回一个键
                        var = rs.getInt(1); // 获得值并赋值给 var 变量
                    }
                } else {
                    var = count; // 如果不需要获得，则将受SQL影像的记录数赋值给 var 变量
                }
                commit(c); // 提交事务
            } catch (SQLException e) {
                System.out.println("数据保存失败: " + e.getMessage());
                rollback(c); // 回滚事务
            }
        }
        return var;
    }

    /** 提交事务 */
    private static void commit(Connection c) {
        if (c != null && !autoCommit) {
            try {
                c.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /** 回滚事务 */
    private static void rollback(Connection c) {
        if (c != null && !autoCommit) {
            try {
                c.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 释放资源
     **/
    public static void release(Object cloaseable) {

        if (cloaseable != null) {

            if (cloaseable instanceof ResultSet) {
                ResultSet rs = (ResultSet) cloaseable;
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (cloaseable instanceof Statement) {
                Statement st = (Statement) cloaseable;
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (cloaseable instanceof Connection) {
                Connection c = (Connection) cloaseable;
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

    }

}