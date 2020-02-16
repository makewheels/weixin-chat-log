package com.eg.weixinchatlog.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 语音工具类
 * silk转pcm和mp3格式
 *
 * @time 2020-02-16 16:01
 */
public class VoiceUtil {

    /**
     * 调用sile_v3_decoder
     * 将silk转成pcm格式
     *
     * @param source
     * @param target
     */
    public static void silkToPcm(File source, File target) {
        //因为sile_v3_decoder不能承受很长路径
        //先将源文件复制到temp目录下，转码后，再复制回去
        String tempFileName = UuidUtil.getUuid();
        File tempDir = SystemUtils.getJavaIoTmpDir();
        File tempSource = new File(tempDir, tempFileName);
        File tempTarget = new File(tempDir, tempFileName + "_result");
        try {
            //复制源文件到临时目录
            FileUtils.copyFile(source, tempSource);
            String silk_v3_decoder_path = VoiceUtil.class.getResource(
                    "/silk_v3_decoder.exe").getPath();
            Process process = Runtime.getRuntime().exec(silk_v3_decoder_path + " \""
                    + tempSource + "\" \"" + tempTarget + "\"");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.print(line + " ");
            }
            BufferedReader bufferedReader1 = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line1;
            while ((line1 = bufferedReader1.readLine()) != null) {
                System.out.println(line1);
            }
            //把临时转码结果复制回去
            FileUtils.copyFile(tempTarget, target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //删除临时文件
        tempSource.delete();
        tempTarget.delete();
    }

    /**
     * 使用ffmpeg
     * pcm转mp3
     *
     * @param source
     * @param target
     */
    public static void pcmToMp3(File source, File target) {
        //ffmpeg -y -f s16le -ar 24000 -ac 1 -i 源文件 目标文件
        try {
            Process process = Runtime.getRuntime().exec(
                    "ffmpeg -y -f s16le -ar 24000 -ac 1 -i \""
                            + source.getAbsolutePath() + "\" \"" + target.getAbsolutePath() + "\""
            );
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        File silk = new File("D:\\Programming\\SoftTopics\\2020.02.15-weixin-decrypt\\phone-data\\weixin\\MicroMsg\\df062cf66082a3f0e79b4f21d7ffed21\\voice2\\14\\99\\msg_4819560215207813c759f0d103.amr");
        File pcm = new File(silk.getParent(), silk.getName() + ".pcm");
        File mp3 = new File(silk.getParent(), silk.getName() + ".mp3");
        silkToPcm(silk, pcm);
        pcmToMp3(pcm, mp3);
        System.out.println(SystemUtils.JAVA_IO_TMPDIR);
    }
}
