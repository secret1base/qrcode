package com.wyj;

import org.apache.http.*;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
/**
 * @ClassName ImageUtils
 * @Description 网络图片下载工具类
 * @Author wyj
 * Date 2019/1/14
 **/
public class ImageUtils {
    public static File urlToImage(String url){
        File urlImgFile=null;
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(url);
        httpget.addHeader("Content-Type", "text/html;charset=UTF-8");
        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(500)
                .setConnectTimeout(0).setSocketTimeout(0).build();
        httpget.setConfig(requestConfig);

        try (CloseableHttpResponse response = httpclient.execute(httpget);
             InputStream urlImgStream = handleResponse(response);) {

            Header[] contentTypeHeader = response.getHeaders("Content-Type");
            if (contentTypeHeader != null && contentTypeHeader.length > 0) {
                if (contentTypeHeader[0].getValue().startsWith(ContentType.APPLICATION_JSON.getMimeType())) {

                    // application/json; encoding=utf-8 下载媒体文件出错
                    String responseContent = handleUTF8Response(response);
                    System.out.println(responseContent);
                }
            }

            urlImgFile = createTmpFile(urlImgStream, "headimg_" + UUID.randomUUID(), "jpg");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("网络图片读取有误");
        } finally {
            httpget.releaseConnection();
        }
        return urlImgFile;
    }
    public static InputStream handleResponse(final HttpResponse response) throws IOException {
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        return entity == null ? null : entity.getContent();
    }
    public static String handleUTF8Response(final HttpResponse response) throws IOException {
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        return entity == null ? null : EntityUtils.toString(entity, Consts.UTF_8);
    }
    public static File createTmpFile(InputStream inputStream, String name, String ext) throws IOException {
        File tmpFile = File.createTempFile(name, '.' + ext);

        tmpFile.deleteOnExit();

        try (FileOutputStream fos = new FileOutputStream(tmpFile)) {
            int read = 0;
            byte[] bytes = new byte[1024 * 100];
            while ((read = inputStream.read(bytes)) != -1) {
                fos.write(bytes, 0, read);
            }

            fos.flush();
            return tmpFile;
        }
    }
}
