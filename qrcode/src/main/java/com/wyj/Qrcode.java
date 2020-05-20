package com.wyj;

import java.io.File;
/**
 * @ClassName Qrcode
 * @Description 二维码
 * @Author wyj
 * Date 2019/1/14
 **/
public class Qrcode {
    /**默认大小*/
    public static final int DEFAULT_SIZE=200;
    public static final int DEFAULT_X=510;
    public static final int DEFAULT_Y=510;
    public static final double DEFAULT_PERCENT=0.8;
    /**二维码内容*/
    private String content;
    /**二维码文件*/
    private File logoFile;
    /**二维码大小*/
    private int size;
    /**二维码所处坐标*/
    private int x;
    private int y;
    /**二维码在图片中的大小*/
    private double percent;
    public Qrcode(String content){
        this.size=DEFAULT_SIZE;
        this.content = content;
        this.x=DEFAULT_X;
        this.y=DEFAULT_Y;
        this.percent=DEFAULT_PERCENT;
    }
    public Qrcode(String content,File logoFile){
        this.size=DEFAULT_SIZE;
        this.logoFile = logoFile;
        this.content = content;
        this.x=DEFAULT_X;
        this.y=DEFAULT_Y;
        this.percent=DEFAULT_PERCENT;
    }

    public Qrcode(String content, File logoFile, int size) {
        this.content = content;
        this.logoFile = logoFile;
        this.size = size;
        this.x=DEFAULT_X;
        this.y=DEFAULT_Y;
        this.percent=DEFAULT_PERCENT;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public File getLogoFile() {
        return logoFile;
    }

    public void setLogoFile(File logoFile) {
        this.logoFile = logoFile;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
