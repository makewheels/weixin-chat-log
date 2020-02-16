package com.eg.weixinchatlog.weixin.dao;

import com.eg.weixinchatlog.weixin.WeixinUser;
import lombok.Data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @time 2020-02-16 11:06
 */
@Data
public class ImgFlagDao {
    private WeixinUser weixinUser;
    private Connection connection;
    private File dbFile;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
