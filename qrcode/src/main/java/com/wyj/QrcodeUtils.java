package com.wyj;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName QrcodeUtils
 * @Description 名片工具类
 * @Author wyj
 * Date 2019/1/14
 **/
public class QrcodeUtils {
    private static final String FORMAT = "png";// 生成二维码的格式
    /**
     * 绘制背景图
     * @author wyj
     * @date 2019/1/14
     * @param
     * @return void
     */
    public static BufferedImage drawbackground(BackgroundImage bg) throws IOException {
        int width=bg.getWidth();
        int height=bg.getHeight();
        int arcHeight = bg.getArcHeight();
        int arcWidth = bg.getArcWidth();
        //创建BufferedImage对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取Graphics2D
        Graphics2D g2d = image.createGraphics();

        // 增加下面代码使得背景透明
        image = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = image.createGraphics();
        // 背景透明代码结束

        //填充白色圆角矩形
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0,0,width,height,arcWidth,arcHeight);// 起点(0, 0), 700, 高700, 圆角宽30, 圆角高30

        // 保存文件
//        ImageIO.write(image, "png", new File("C:\\\\Users\\\\Administrator\\\\Desktop\\\\image\\\\a.jpg"));
        return image;
    }

    /**
     * @Description 绘制名片
     * @author wyj
     * @date 2019/1/14
     * @param storeName
     * @param qrcodeConfig
     * @param topLeft
     * @param topRight
     * @param midRight
     * @param logo
     * @param outPath
     * @throws Exception
     */
    public static void drawImage(StoreName storeName,Qrcode qrcodeConfig,Image topLeft,Image topRight,Image midRight,Image logo,String outPath)throws Exception{
        //生成背景图片
        BackgroundImage backgroundImage = new BackgroundImage();
        int bgWidth=backgroundImage.getWidth();
        int bgHeight = backgroundImage.getHeight();
        BufferedImage bg = drawbackground(backgroundImage);
        //生成二维码图片
        byte[] qrcode = createQrcode(qrcodeConfig);//二维码内容,二维码长度，二维码logo
        //将二维码图片绘制到背景图片中
        ByteArrayInputStream bats=new ByteArrayInputStream(qrcode);
        BufferedImage qrimg =ImageIO.read(bats);
        Graphics2D g = bg.createGraphics();
        // 绘制二维码图
        int size = qrcodeConfig.getSize();
        double percent = qrcodeConfig.getPercent();
        int round = (int)Math.round(size * percent);
        g.drawImage(qrimg, qrcodeConfig.getX(), qrcodeConfig.getY(), round, round, null);
        g.dispose();
        //绘制图一
        g = bg.createGraphics();
        InputStream fis1=null;
        if(topLeft!=null&&topLeft.getImageFile()!=null){
            File imageFile = topLeft.getImageFile();
            fis1=new FileInputStream(imageFile);
        }else{
            fis1=QrcodeUtils.class.getClass().getResourceAsStream("/1.jpg");
        }
        BufferedImage pic1= ImageIO.read(fis1);
        int width1= (int)Math.round(bgWidth * 0.665);
        int height1= (int)Math.round(bgHeight * 0.657);
        g.drawImage(pic1, 0, 0, width1, height1, null);
        g.dispose();
        //绘制图二
        InputStream fis2=null;
        if(topRight!=null&&topRight.getImageFile()!=null){
            File imageFile = topRight.getImageFile();
            fis2=new FileInputStream(imageFile);
        }else{
            fis2= QrcodeUtils.class.getClass().getResourceAsStream("/2.jpg");
        }
        g = bg.createGraphics();
        int x2= (int)Math.round(bgWidth * 0.671);
        int width2= (int)Math.round(bgWidth * 0.328);
        int height2= (int)Math.round(bgHeight * 0.325);
        BufferedImage pic2=ImageIO.read(fis2);
        g.drawImage(pic2, x2, 0, width2, height2, null);
        g.dispose();
        //绘制图三
        InputStream fis3=null;
        if(midRight!=null&&midRight.getImageFile()!=null){
            File imageFile = midRight.getImageFile();
            fis3=new FileInputStream(imageFile);
        }else{
            fis3= QrcodeUtils.class.getClass().getResourceAsStream("/3.jpg");
        }
        g = bg.createGraphics();
        int x3= (int)Math.round(bgWidth * 0.671);
        int y3= (int)Math.round(bgWidth * 0.331);
        int width3= (int)Math.round(bgWidth * 0.328);
        int height3= (int)Math.round(bgHeight * 0.325);
        BufferedImage pic3=ImageIO.read(fis3);
        g.drawImage(pic3, x3, y3, width3, height3, null);
        g.dispose();
        //绘制logo
        InputStream logois=null;
        if(logo!=null&logo.getImageFile()!=null){
            File imageFile = logo.getImageFile();
            logois=new FileInputStream(imageFile);
        }else{
            logois = QrcodeUtils.class.getClass().getResourceAsStream("/logo.jpg");
        }
        g = bg.createGraphics();
        BufferedImage logoimg=ImageIO.read(logois);
        //按比例缩放
        int fixedHeight=(int)Math.round(0.142*bgHeight);
        int height = logoimg.getHeight();
        int width = logoimg.getWidth();
        int newWidth = (int)(Double.parseDouble(Integer.toString(fixedHeight)) / Double.parseDouble(Integer.toString(height))* width);
        int x4=(int)Math.round(bgWidth * 0.028);
        int y4=(int)Math.round(bgWidth * 0.685);
        g.drawImage(logoimg, x4, y4, newWidth, fixedHeight, null);
        g.dispose();
        // 绘制文字
        int space=(int)Math.round(bgWidth * 0.014);
        int fontHeight=(int)Math.round(bgWidth * 0.725);
        g=bg.createGraphics();
        g.setColor(storeName.getColor());// 文字颜色
        g.setFont(storeName.getFont());
        g.drawString(storeName.getName(),x4+newWidth+space, fontHeight);

        g.dispose();
        // 写入logo图片到二维码
        File bgFile = Files.createTempFile("bg_", ".jpg").toFile();
        ImageIO.write(bg, "png", bgFile);
        String path = bgFile.getPath();
        //图片圆角,受处理图片必须为1:1图片
        makeCircularImg(path,outPath,bgWidth,backgroundImage.getArcWidth());

    }
    public static void main(String[] args) throws Exception {
        File logoFile = ImageUtils.urlToImage("http://p5.so.qhimgs1.com/bdr/_240_/t01fadcf3cc106e7afb.jpg");
        //测试1：全部默认值
//        Qrcode qrcode=new Qrcode("www.baidu.com",null,200);
//        Image topLeft=new Image();
//        Image topRight=new Image();
//        Image midRight=new Image();
//        Image logo=new Image();
//        StoreName storeName=new StoreName("三只松鼠");
        //测试2：网络图片
        Qrcode qrcode=new Qrcode("www.baidu.com",logoFile,200);
        Image topLeft=new Image();
        topLeft.setUrlImage("http://p5.so.qhimgs1.com/bdr/_240_/t01f1d5900cb339907b.jpg");
        Image topRight=new Image();
        topRight.setUrlImage("http://p2.so.qhimgs1.com/bdr/_240_/t0128ebf20d8a62ed1e.jpg");
        Image midRight=new Image();
        midRight.setUrlImage("http://p0.so.qhimgs1.com/bdr/_240_/t015e666ede80f90fa0.jpg");
        Image logo=new Image();
        logo.setUrlImage("http://p1.so.qhimgs1.com/bdr/_240_/t014e74967f0fae4111.jpg");
        StoreName storeName=new StoreName("三只松鼠");
        String outPath="C:\\Users\\Lenovo\\Desktop\\newqrcode\\card.jpg";
        drawImage(storeName,qrcode,topLeft,topRight,midRight,logo,outPath);
    }
    /**
     * 根据内容生成二维码数据
     * @param content 二维码文字内容[为了信息安全性，一般都要先进行数据加密]
     * @param length 二维码图片宽度和高度
     */
    private static BitMatrix createQrcodeMatrix(String content, int length) {
        Map<EncodeHintType, Object> hints = Maps.newEnumMap(EncodeHintType.class);
        // 设置字符编码
        hints.put(EncodeHintType.CHARACTER_SET, Charsets.UTF_8.name());
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 1);

        try {
            return new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, length, length, hints);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 创建二维码
     * @author wyj
     * @date 2019/1/14
     * @param qrcodeConfig
     * @return byte[]
     */
    public static byte[] createQrcode(Qrcode qrcodeConfig) {
        String content=qrcodeConfig.getContent();
        int length=qrcodeConfig.getSize();
        File logoFile=qrcodeConfig.getLogoFile();
        if (logoFile != null && !logoFile.exists()) {
            throw new IllegalArgumentException("请提供正确的logo文件！");
        }

        BitMatrix qrCodeMatrix = createQrcodeMatrix(content, length);
        if (qrCodeMatrix == null) {
            throw new IllegalArgumentException("请提供正确的二维码图片地址");
        }
        try {
            File file = Files.createTempFile("qrcode_" + UUID.randomUUID(), "." + FORMAT).toFile();

            MatrixToImageWriter.writeToFile(qrCodeMatrix, FORMAT, file);
            if (logoFile != null) {
                // 添加logo图片, 此处一定需要重新进行读取，而不能直接使用二维码的BufferedImage 对象
                BufferedImage img = ImageIO.read(file);
                overlapImage(img, FORMAT, file.getAbsolutePath(), logoFile, new MatrixToLogoImageConfig());
            }

            return toByteArray(file);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 将文件转换为字节数组， 使用MappedByteBuffer，可以在处理大文件时，提升性能
     *
     * @param file
     *            文件
     * @return 二维码图片的字节数组
     */
    private static byte[] toByteArray(File file) {
        try (FileChannel fc = new RandomAccessFile(file, "r").getChannel();) {
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).load();
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 覆盖logo
     * @author wyj
     * @date 11:30 2019/1/14
     * @param image
     * @param format
     * @param imagePath
     * @param logoFile
     * @param logoConfig
     * @return void
     */
    private static void overlapImage(BufferedImage image, String format, String imagePath, File logoFile,
                                     MatrixToLogoImageConfig logoConfig) throws Exception {
        try {
            BufferedImage logo = ImageIO.read(logoFile);
            Graphics2D g = image.createGraphics();
            // 考虑到logo图片贴到二维码中，建议大小不要超过二维码的1/5;
            int width = image.getWidth() / logoConfig.getLogoPart();
            int height = image.getHeight() / logoConfig.getLogoPart();
            // logo起始位置，此目的是为logo居中显示
            int x = (image.getWidth() - width) / 2;
            int y = (image.getHeight() - height) / 2;
            // 绘制图
            g.drawImage(logo, x, y, width, height, null);

            g.dispose();
            // 写入logo图片到二维码
            ImageIO.write(image, format, new File(imagePath));
        } catch (Exception e) {
            throw new Exception("二维码添加logo时发生异常！", e);
        }
    }
    public static String makeCircularImg(String srcFilePath, String circularImgSavePath,int targetSize, int cornerRadius) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(srcFilePath));
        BufferedImage circularBufferImage = roundImage(bufferedImage,targetSize,cornerRadius);
        ImageIO.write(circularBufferImage, "png", new File(circularImgSavePath));
        return circularImgSavePath;
    }

    /**
     * 图片圆角
     * @param image
     * @param targetSize
     * @param cornerRadius
     * @return
     */
    private static BufferedImage roundImage(BufferedImage image, int targetSize, int cornerRadius) {
        BufferedImage outputImage = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = outputImage.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, targetSize, targetSize, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return outputImage;
    }
}
