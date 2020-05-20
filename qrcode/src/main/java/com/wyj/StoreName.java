package com.wyj;

import java.awt.*;
/**
 * @ClassName StoreName
 * @Description 名片描述
 * @Author wyj
 * Date 2019/1/14 0014 17:16
 **/
public class StoreName {
    private static final Font DEFAULT_FONT=new Font("宋体", Font.BOLD, 28);
    private static final Color DEFAULT_COLOR=Color.black;
    private Font font;
    private String name;
    private Color color;

    public StoreName(String name) {
        this.name = name;
        this.font=DEFAULT_FONT;
        this.color=DEFAULT_COLOR;
    }

    public StoreName(Font font, String name,Color color) {
        this.font = font;
        this.name = name;
        this.color = color;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
