package com.eg.weixinchatlog.sqlite;

import java.io.File;
import java.sql.*;

/**
 * @time 2020-02-15 15:53
 */
public class EnMicroMsgUtil {
    private static Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void initConnection(File dbFile) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
