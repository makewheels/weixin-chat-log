package com.eg.weixinchatlog.weixin.sqlite.enmicromsg.dao;

import com.eg.weixinchatlog.weixin.sqlite.enmicromsg.ImgFlag;
import com.eg.weixinchatlog.weixin.sqlite.enmicromsg.Message;
import com.eg.weixinchatlog.weixin.sqlite.enmicromsg.Rcontact;
import lombok.Data;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @time 2020-02-15 15:53
 */
@Data
public class EnMicroMsgDao {
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
     * 获取联系人列表
     *
     * @return
     */
    public List<Rcontact> getRcontactList() {
        List<Rcontact> rcontactList = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("select * from rcontact");
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Rcontact rcontact = new Rcontact();
            try {
                rcontact.setUsername(resultSet.getString("username"));
                rcontact.setAlias(resultSet.getString("alias"));
                rcontact.setConRemark(resultSet.getString("conRemark"));
                rcontact.setDomainList(resultSet.getString("domainList"));
                rcontact.setNickname(resultSet.getString("nickname"));
                rcontact.setPyInitial(resultSet.getString("pyInitial"));
                rcontact.setQuanPin(resultSet.getString("quanPin"));
                rcontact.setShowHead(resultSet.getInt("showHead"));
                rcontact.setType(resultSet.getInt("type"));
                rcontact.setWeiboFlag(resultSet.getInt("weiboFlag"));
                rcontact.setWeiboNickname(resultSet.getString("weiboNickname"));
                rcontact.setConRemarkPyFull(resultSet.getString("conRemarkPyFull"));
                rcontact.setConRemarkPyShort(resultSet.getString("conRemarkPyShort"));
                rcontact.setLvbuff(resultSet.getBytes("lvbuff"));
                rcontact.setVerifyFlag(resultSet.getInt("verifyFlag"));
                rcontact.setEncryptUsername(resultSet.getString("encryptUsername"));
                rcontact.setChatroomFlag(resultSet.getInt("chatroomFlag"));
                rcontact.setDeleteFlag(resultSet.getInt("deleteFlag"));
                rcontact.setContactLabelIds(resultSet.getString("contactLabelIds"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                rcontact.setTicket(resultSet.getString("ticket"));
            } catch (SQLException e) {
            }
            try {
                rcontact.setOpenImAppid(resultSet.getString("openImAppid"));
                rcontact.setDescWordingId(resultSet.getString("descWordingId"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                rcontact.setSourceExtInfo(resultSet.getString("sourceExtInfo"));
            } catch (SQLException e) {
            }
            rcontactList.add(rcontact);
        }
        try {
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rcontactList;
    }

    /**
     * 根据talker获取，与改联系人的聊天记录总数
     *
     * @param username
     * @return
     */
    public long getMessageCountByTalker(String username) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select count(*) from message where talker=?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            long count = resultSet.getLong(1);
            resultSet.close();
            preparedStatement.close();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据talker获取，与改联系人所有聊天记录
     *
     * @param username
     * @return
     */
    public List<Message> getAllMessageByTalker(String username) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from message where talker=?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Message> messageList = new ArrayList<>();
            while (resultSet.next()) {
                Message message = new Message();
                message.setMsgId(resultSet.getLong("msgId"));
                message.setMsgSvrId(resultSet.getLong("msgSvrId"));
                message.setType(resultSet.getLong("type"));
                message.setStatus(resultSet.getInt("status"));
                message.setIsSend(resultSet.getInt("isSend"));
                message.setIsShowTimer(resultSet.getInt("isShowTimer"));
                message.setCreateTime(resultSet.getLong("createTime"));
                message.setTalker(resultSet.getString("talker"));
                message.setContent(resultSet.getString("content"));
                message.setImgPath(resultSet.getString("imgPath"));
                message.setReserved(resultSet.getString("reserved"));
                message.setLvbuffer(resultSet.getBytes("lvbuffer"));
                message.setTransContent(resultSet.getString("transContent"));
                message.setTransBrandWording(resultSet.getString("transBrandWording"));
                message.setTalkerId(resultSet.getInt("talkerId"));
                message.setBizClientMsgId(resultSet.getString("bizClientMsgId"));
                message.setBizChatId(resultSet.getInt("bizChatId"));
                message.setBizChatUserId(resultSet.getString("bizChatUserId"));
                message.setMsgSeq(resultSet.getLong("msgSeq"));
                message.setFlag(resultSet.getInt("flag"));
                messageList.add(message);
            }
            resultSet.close();
            preparedStatement.close();
            return messageList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查找img_flag表
     * 根据username查找
     *
     * @param username
     * @return
     */
    public ImgFlag getImgFlagByUsername(String username) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from img_flag where username=?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() == false) {
                return null;
            }
            ImgFlag imgFlag = new ImgFlag();
            imgFlag.setUsername(resultSet.getString("username"));
            imgFlag.setImgflag(resultSet.getLong("imgflag"));
            imgFlag.setLastupdatetime(resultSet.getLong("lastupdatetime"));
            imgFlag.setReserved1(resultSet.getString("reserved1"));
            imgFlag.setReserved2(resultSet.getString("reserved2"));
            imgFlag.setReserved3(resultSet.getLong("reserved3"));
            imgFlag.setReserved4(resultSet.getLong("reserved4"));
            resultSet.close();
            preparedStatement.close();
            return imgFlag;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
