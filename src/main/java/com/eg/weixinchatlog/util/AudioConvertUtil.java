package com.eg.weixinchatlog.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 支持在windows系统下，amr、aud、slk、silk转成mp3格式
 *
 * @time 2020-02-16 16:01
 */
public class AudioConvertUtil {
    /**
     * 调用sile_v3_decoder.exe
     * 将silk转成pcm格式
     *
     * @param source
     * @param target
     */
    public static void silkToPcm(File source, File target) {
        List<String> commend = new ArrayList<>();
        String silk_v3_decoder_path = AudioConvertUtil.class.getResource(
                "/silk_v3_decoder.exe").getPath();
        commend.add(silk_v3_decoder_path);
        String sourcePath = source.getAbsolutePath();
        sourcePath = sourcePath.replace("\\", "/");
        commend.add(sourcePath);
        String targetPath = target.getAbsolutePath();
        targetPath = targetPath.replace("\\", "/");
        commend.add(targetPath);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process process = builder.start();
            process.waitFor();
            //取得命令结果的输出流
            InputStream fis = process.getInputStream();
            //用一个读输出流类去读
            InputStreamReader isr = new InputStreamReader(fis, "gbk");
            //用缓冲器读行
            BufferedReader br = new BufferedReader(isr);
            String line;
            //直到读完为止
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 调用ffmpeg，pcm转mp3
    public static void pcmToMp3(File source, File target) {
        //ffmpeg -y -f s16le -ar 24000 -ac 1 -i 源文件 目标文件
        List<String> commend = new ArrayList<>();
        commend.add("ffmpeg");
        commend.add("-y");
        commend.add("-f");
        commend.add("s16le");
        commend.add("-ar");
        commend.add("24000");
        commend.add("-ac");
        commend.add("1");
        commend.add("-i");
        commend.add(source.getAbsolutePath());
        commend.add(target.getAbsolutePath());
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process process = builder.start();
            process.waitFor();
            //取得命令结果的输出流
            InputStream fis = process.getInputStream();
            //用一个读输出流类去读
            InputStreamReader isr = new InputStreamReader(fis);
            //用缓冲器读行
            BufferedReader br = new BufferedReader(isr);
            String line;
            //直到读完为止
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        File silk = new File("D:\\Programming\\SoftTopics\\2020.02.15-weixin-decrypt\\phone-data\\weixin\\MicroMsg\\df062cf66082a3f0e79b4f21d7ffed21\\voice2\\14\\99\\msg_4819560215207813c759f0d103.amr");
        File silk = new File("C:\\Users\\msg_4819560215207813c759f0d103.amr");
        File pcm = new File(silk.getParent(), silk.getName() + ".pcm");
        File mp3 = new File(silk.getParent(), silk.getName() + ".mp3");
        silkToPcm(silk, pcm);
        pcmToMp3(pcm, mp3);
    }
}
