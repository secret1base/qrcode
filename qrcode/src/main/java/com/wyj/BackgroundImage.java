package com.wyj;
/**
 * @ClassName BackgroundImage
 * @Description 背景图片参数
 * @Author wyj
 * Date 2019/1/14
 **/
public class BackgroundImage {
    /**默认宽度*/
    public static final int DEFAULT_HEIGHT=700;
    /**默认高度*/
    public static final int DEFAULT_WIDTH=700;
    /**默认圆角高度*/
    public static final int DEFAULT_ARCHEIGHT=60;
    /**默认圆角宽度*/
    public static final int DEFAULT_ARCWIDTH=60;
    private int width;
    private int height;
    private int arcWidth;
    private int arcHeight;

    BackgroundImage(){
        width=DEFAULT_WIDTH;
        height=DEFAULT_HEIGHT;
        arcWidth=DEFAULT_ARCWIDTH;
        arcHeight=DEFAULT_ARCHEIGHT;
    }
    BackgroundImage(int width,int height){
        this.width=width;
        this.height=height;
        this.arcWidth=DEFAULT_ARCWIDTH;
        this.arcHeight=DEFAULT_ARCHEIGHT;
    }
    BackgroundImage(int width,int height,int arcWidth,int arcHeight){
        this.width=width;
        this.height=height;
        this.arcWidth=arcWidth;
        this.arcHeight=arcHeight;
    }
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getArcWidth() {
        return arcWidth;
    }

    public void setArcWidth(int arcWidth) {
        this.arcWidth = arcWidth;
    }

    public int getArcHeight() {
        return arcHeight;
    }

    public void setArcHeight(int arcHeight) {
        this.arcHeight = arcHeight;
    }
}
