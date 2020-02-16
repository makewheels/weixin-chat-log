package com.eg.weixinchatlog.util;

/**
 * 微信消息类型
 * 参考：
 * https://blog.csdn.net/ziyunyang/article/details/81534560
 *
 * @time 2020-02-15 23:17
 */
public class MessageType {
    public static long TEXT = 1;
    public static long IMAGE = 3;
    public static long VOICE = 34;

    public static long MAIL = 35;//推送邮件
    public static long FRIEND_CONFIRM = 37;     //好友确认
    public static long POSSIBLE_FRIEND_MSG = 40;
    public static long BUSINESS_CARD = 42;      //名片
    public static long VIDEO = 43;              //视频
    public static long FACIAL_EXPRESSION = 47;  //动画表情
    public static long POSITION = 48;           //位置
    public static long SHARE_LINK = 49;         //分享链接，文件也是它
    public static long VOIP_MSG = 50;           //音视频通话
    public static long WEIXIN_INIT = 51;        //微信初始化
    public static long VOIP_NOTIFY = 52;
    public static long VOIP_INVITE = 53;
    public static long SHORT_VIDEO = 62;        //小视频
    public static long SYSNOTICE = 9999;
    public static long SYSTEM = 10000;          //系统
    public static long RECALL = 10002;          //撤回
    public static long INVOICE = 452984881;     //发票

    /**
     * 子消息类型，对应WxFileIndex.db中的msgSubType字段
     */
    public static class MessageSubType {
        public static long VIDEO = 1;       //视频
        public static long VIDEO_COVER = 2; //视频封面
        public static long VOICE = 10;      //语音
        public static long IMAGE_BIG = 20;  //原图

    }
}
