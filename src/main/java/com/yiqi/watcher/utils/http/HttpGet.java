package com.yiqi.watcher.utils.http;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @author Wang Hongyu
 * @title: HttpGet
 * @projectName watcher-robot
 * @description: http Get请求
 * @date 2020/10/27
 */
@Slf4j
@Component
public class HttpGet implements Serializable {
    private static final long serialVersionUID = 6777685163250860757L;

    public JSONObject sendRequest(String url){
        String resultStr = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                resultStr += line;
            }

        } catch (MalformedURLException e) {
            log.error("URL转换失败,错误信息为{}",e.getMessage());
        } catch (IOException e){
            log.error("发送Get请求出错，错误信息为{}",e.getMessage());
        } finally {
            try {
                if (null != in)
                    in.close();
            }catch (IOException e){
                log.error("关闭输入流报错，错误信息为{}",e.getMessage());
            }
        }
        return JSONObject.parseObject(resultStr);
    }
}
