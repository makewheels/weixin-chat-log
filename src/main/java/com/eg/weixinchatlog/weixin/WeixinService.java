package com.eg.weixinchatlog.weixin;

import com.eg.weixinchatlog.weixin.bean.enmicromsg.Message;
import com.eg.weixinchatlog.weixin.bean.enmicromsg.Rcontact;
import com.eg.weixinchatlog.weixin.bean.wxfileindex.WxFileIndex2;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @time 2020-02-15 18:21
 */
@Data
public class WeixinService {
    private WeixinUser weixinUser;
    private EnMicroMsgDao enMicroMsgDao;
    private WxFileIndex2Dao wxFileIndex2Dao;

    /**
     * 初始化联系人列表
     */
    public void initRcontactList() {
        List<Rcontact> rcontactList = enMicroMsgDao.getRcontactList();
        weixinUser.setRcotactList(rcontactList);
    }

    /**
     * 初始化朋友列表
     */
    public void initFriendList() {
        List<Rcontact> friendList = new ArrayList<>();
        for (Rcontact rcontact : weixinUser.getRcotactList()) {
            String username = rcontact.getUsername();
            if (username.contains("@qqim"))
                continue;
            if (username.contains("@app"))
                continue;
            if (username.contains("@chatroom"))
                continue;
//                if (username.startsWith("gh_"))
//                    continue;
            if (rcontact.getType() == 33)
                continue;
            if (username.equals("filehelper"))
                continue;
            if (rcontact.getType() == 65536)
                continue;
            String alias = rcontact.getAlias();
            if (StringUtils.isEmpty(username) && StringUtils.isEmpty(alias))
                continue;
            if (rcontact.getVerifyFlag() != 0)
                continue;
            friendList.add(rcontact);
        }
        weixinUser.setFriendList(friendList);
    }

    /**
     * 据talker获取，与改联系人的聊天记录总数
     *
     * @param username
     * @return
     */
    public long getMessageCountByTalker(String username) {
        return enMicroMsgDao.getMessageCountByTalker(username);
    }

    /**
     * 根据talker获取，与改联系人所有聊天记录
     *
     * @param username
     * @return
     */
    public List<Message> getAllMessageByTalker(String username) {
        return enMicroMsgDao.getAllMessageByTalker(username);
    }

    /**
     * 通过msgId找所有文件列表
     *
     * @param msgId
     * @return
     */
    public List<WxFileIndex2> getAllWeixinFileByMsgId(long msgId) {
        return wxFileIndex2Dao.getAllWeixinFileByMsgId(msgId);
    }

    /**
     * 通过msgId找，size最大文件
     *
     * @param msgId
     * @return
     */
    public WxFileIndex2 getMaxSizeWeixinFileByMsgId(long msgId) {
        List<WxFileIndex2> wxFileIndex2List = wxFileIndex2Dao.getAllWeixinFileByMsgId(msgId);
        if (wxFileIndex2List.size() == 1) {
            return wxFileIndex2List.get(0);
        }
        int maxIndex = 0;
        long size = wxFileIndex2List.get(0).getSize();
        for (int i = 1; i < wxFileIndex2List.size(); i++) {
            WxFileIndex2 wxFileIndex2 = wxFileIndex2List.get(i);
            long eachSize = wxFileIndex2.getSize();
            if (eachSize > size) {
                size = eachSize;
                maxIndex = i;
            }
        }
        return wxFileIndex2List.get(maxIndex);
    }
}
