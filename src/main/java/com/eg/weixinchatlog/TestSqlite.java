package com.eg.weixinchatlog;

import java.sql.*;

/**
 * @time 2020-02-15 15:38
 */
public class TestSqlite {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String db = "D:\\Programming\\SoftTopics\\2020.02.15微信解密\\微信数据库\\dat.db";
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + db);
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery("select * from message;"); //查询数据
        while (rs.next()) { //将查询到的数据打印出来
            System.out.print("name = " + rs.getString("msgId") + " "); //列属性一
            System.out.println("age = " + rs.getString("content")); //列属性二
        }
        rs.close();
        conn.close();

    }
}
