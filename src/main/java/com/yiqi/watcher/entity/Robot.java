package com.yiqi.watcher.entity;

import java.io.Serializable;

/**
 * @author Wang Hongyu
 * @title: Robot
 * @projectName watcher-robot
 * @description: TODO
 * @date 2020/10/28
 */
public abstract class Robot implements Serializable {

    private static final long serialVersionUID = -4459349223423265401L;

    /**
     * 运行方法
     */
    abstract public void run();

    /**
     * 发送战报
     * @param report
     */
    abstract public void sendMessage(String report);
}
