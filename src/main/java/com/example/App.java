package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class App {
    public static void main(String[] args) {
        URL url = null;
        try {
            // 解析目标页面
            url = new URL("https://mp.weixin.qq.com/s/YPrqMOYYrAtCni2VT8c4jA?");
            Document document = Jsoup.parse(url, 3000);
            Elements imgs = document.getElementsByTag("img");
            int index = 0;
            for (Element img : imgs) {
                // 获取图片地址
                String src = img.attr("data-src");
                if (src == null || src.length() == 0) {
                    System.out.println("无图片地址");
                    continue;
                }
                System.out.println(src);
                // 打开连接
                URL target = new URL(src);
                URLConnection connection = target.openConnection();
                // 本地路径
                String localPath = System.getProperty("user.dir") + "\\images\\" + index + ".jpg";
                // 构造输入流、输出流
                try(InputStream is = connection.getInputStream();
                    FileOutputStream fos = new FileOutputStream(localPath)) {
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    System.out.println("下载完成" + index++);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
