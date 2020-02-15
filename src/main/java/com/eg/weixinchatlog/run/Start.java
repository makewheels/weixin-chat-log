package com.eg.weixinchatlog.run;

import com.eg.weixinchatlog.weixin.EnMicroMsgDao;
import com.eg.weixinchatlog.util.CalcUtil;
import com.eg.weixinchatlog.util.Constants;
import com.eg.weixinchatlog.weixin.WeixinUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @time 2020-02-15 16:52
 */
public class Start {
    //select msgId,msgSvrId,type,status,isSend,createTime,talker,content,imgPath,reserved,talkerId,bizClientMsgId,msgSeq from message
    private List<WeixinUser> weixinUserList = new ArrayList<>();

    /**
     * 初始化微信用户列表
     */
    private void initWeixinUserList() {
        List<String> uinList = CalcUtil.getUinList();
        for (String uin : uinList) {
            WeixinUser weixinUser = new WeixinUser();
            weixinUser.setUin(uin);
            String mmUinMd5 = CalcUtil.getMmUinMd5(uin);
            weixinUser.setMmuinMd5(mmUinMd5);
            weixinUser.setSqlitePassword(CalcUtil.getSqlitePassword(uin));
            weixinUser.setMmFolder(new File(Constants.DATA_PATH + "/MicroMsg/" + mmUinMd5));
            //设置EnMicroMsg.db文件
            File enMicroMsg_decrypt_db_file = new File(Constants.DATA_PATH + "/MicroMsg/"
                    + mmUinMd5 + "/EnMicroMsg-decrypt.db");
            EnMicroMsgDao enMicroMsgDao = new EnMicroMsgDao();
            enMicroMsgDao.setDbFile(enMicroMsg_decrypt_db_file);
            weixinUser.setEnMicroMsgDao(enMicroMsgDao);
            weixinUserList.add(weixinUser);
        }
    }

    public static void main(String[] args) {
        Start start = new Start();
        start.initWeixinUserList();

    }
}
