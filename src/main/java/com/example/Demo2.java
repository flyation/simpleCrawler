package com.example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 使用Jsoup.connect()访问页面获取org.jsoup.Connection，再设置请求头，再用Jsoup.parse()进行解析
 */
public class Demo2 {
    public static void main(String[] args) {
        URL url = null;
        try {
            // 获取连接
            Connection connect = Jsoup.connect("https://www.baidu.com");
            // 添加请求头
            String cookie = "name=zhangsang;age=18";
            connect.header("cookie", cookie);
            // 解析页面
            Document document = connect.get();
            Element root = document.getElementById("root");
            Elements imgs = root.getElementsByTag("img");
            int index = 0;
            for (Element img : imgs) {
                String src = img.attr("src");
                if (src == null || src.length() == 0) {
                    System.out.println("无图片地址");
                    continue;
                }
                // 打开图片连接
                URL target = new URL(src);
                URLConnection connection = target.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36");
//                connection.setReadTimeout(3000);
                String name = "img" + index;
                // 本地路径
                String localPath = System.getProperty("user.dir") + "\\images\\Demo2\\" + name + ".jpg";
                // 构造输入流、输出流
                try(InputStream is = connection.getInputStream();
                    FileOutputStream fos = new FileOutputStream(localPath)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) != -1) { // 读
                        fos.write(buffer, 0, len);      // 写
                    }
                    System.out.println("下载完成：" + index++);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("【下载任务完成】");
    }
}
