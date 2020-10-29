package com.yiqi.watcher.service;

import com.alibaba.fastjson.JSONObject;
import com.yiqi.watcher.utils.http.HttpPost;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wang Hongyu
 * @title: QQRobotService
 * @projectName watcher-robot
 * @description: TODO
 * @date 2020/10/28
 */
@Component
@Slf4j
@Setter
@Getter
public class QQRobotService implements QQRobotIService {
    private static final long serialVersionUID = -4517588175846843937L;

    @Value("${steam.dota2Robot.webIp}")
    private String webIp;

    private final String sessionUrl = "http://{0}/auth";
    private final String verifyUrl = "http://{0}/verify";
    private final String sendGroupMessageUrl = "http://{0}/sendGroupMessage";
    private final String releaseUrl = "http://{0}/release";
    @Autowired
    private HttpPost httpPost;

    @Override
    public String getSession(String authKey) {
        JSONObject paramsJSON = new JSONObject();
        try {
            paramsJSON.put("authKey",authKey);
        }catch (Exception e){
            log.error("获取QQ机器人session失败，报错信息为{}",e.getMessage());
        }
        return httpPost.sendRequest(MessageFormat.format(sessionUrl,webIp),paramsJSON.toJSONString()).getString("session");
    }

    @Override
    public boolean verify(String session, String qq) {
        JSONObject paramsJSON = new JSONObject();
        try {
            paramsJSON.put("sessionKey",session);
            paramsJSON.put("qq",qq);
        }catch (Exception e){
            log.error("QQ机器人验证身份信息失败,报错信息为{}",e.getMessage());
        }
        return httpPost.sendRequest(MessageFormat.format(verifyUrl,webIp),paramsJSON.toJSONString()).getString("msg").equals("success");
    }

    @Override
    public void sendGroupMessage(String authKey, String qq, String target, String message) {
        JSONObject paramsJSON = new JSONObject();
        Map<String,String> msg = new HashMap<>();
        msg.put("type","Plain");
        msg.put("text",message);
        List<Map<String,String>> msgList = new ArrayList<>();
        msgList.add(msg);
        String session = getSession(authKey);
        boolean verify = verify(session,qq);
        paramsJSON.put("sessionKey",session);
        paramsJSON.put("target",target);
        paramsJSON.put("messageChain",msgList);
        if (verify)
            try {
                httpPost.sendRequest(MessageFormat.format(sendGroupMessageUrl,webIp),paramsJSON.toJSONString());
            }catch (Exception e){
                log.error("QQ机器人发送信息失败，报错信息为{}",e.getMessage());
            }
        release(session,qq);
    }

    @Override
    public void release(String session, String qq) {
        JSONObject paramsJSON = new JSONObject();
        try {
            paramsJSON.put("sessionKey",session);
            paramsJSON.put("qq",qq);
            httpPost.sendRequest(MessageFormat.format(releaseUrl,webIp),paramsJSON.toJSONString());
        }catch (Exception e){
            log.error("QQ机器人资源释放失败，报错信息为{}",e.getMessage());
        }
    }
}
