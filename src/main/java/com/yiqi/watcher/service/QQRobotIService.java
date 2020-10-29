package com.yiqi.watcher.service;

import java.io.Serializable;

/**
 * @author Wang Hongyu
 * @title: QQRobotIService
 * @projectName watcher-robot
 * @description: TODO
 * @date 2020/10/28
 */
public interface QQRobotIService extends Serializable {
    /**
     * 获取Session
     * @param authKey
     * @return
     */
    String getSession(String authKey);

    /**
     * 身份校验
     * @param session
     * @param qq
     * @return
     */
    boolean verify(String session,String qq);

    /**
     * 向指定QQ群发送消息
     * @param authKey
     * @param qq
     * @param target
     * @param message
     */
    void sendGroupMessage(String authKey,String qq,String target,String message);

    /**
     * 资源释放
     * @param session_key
     * @param qq
     */
    void release(String session_key,String qq);
}
