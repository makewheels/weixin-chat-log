package com.eg.weixinchatlog.weixin;

import com.eg.weixinchatlog.weixin.bean.wxfileindex.WxFileIndex2;
import lombok.Data;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @time 2020-02-16 00:09
 */
@Data
public class WxFileIndex2Dao {
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

    /**
     * 根据消息id找文件信息
     *
     * @param msgId
     * @return
     */
    public List<WxFileIndex2> getAllWxFileIndex2ByMsgId(long msgId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from WxFileIndex2 where msgId=?");
            preparedStatement.setLong(1, msgId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<WxFileIndex2> wxFileIndex2List = new ArrayList<>();
            while (resultSet.next()) {
                WxFileIndex2 wxFileIndex2 = new WxFileIndex2();
                wxFileIndex2.setMsgId(resultSet.getString("msgId"));
                wxFileIndex2.setUsername(resultSet.getString("username"));
                wxFileIndex2.setMsgType(resultSet.getLong("msgType"));
                wxFileIndex2.setMsgSubType(resultSet.getLong("msgSubType"));
                wxFileIndex2.setPath(resultSet.getString("path"));
                wxFileIndex2.setSize(resultSet.getLong("size"));
                wxFileIndex2.setMsgtime(resultSet.getString("msgtime"));
                wxFileIndex2List.add(wxFileIndex2);
            }
            return wxFileIndex2List;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
