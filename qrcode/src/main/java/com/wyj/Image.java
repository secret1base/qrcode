package com.wyj;

import java.io.File;
/**
 * @ClassName Image
 * @Description 图片参数
 * @Author wyj
 * Date 2019/1/14
 **/
public class Image {
    private File imageFile;
    private String urlImage;

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        File file = ImageUtils.urlToImage(urlImage);
        imageFile=file;
        this.urlImage = urlImage;
    }
}
