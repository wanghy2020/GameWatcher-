package com.yiqi.watcher.service;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @author Wang Hongyu
 * @title: QueryMatchInfoIService
 * @projectName watcher-robot
 * @description: TODO
 * @date 2020/10/28
 */
public interface QueryMatchInfoIService extends Serializable {
    /**
     * 获取最新的比赛id
     * @param account
     * @return
     */
    String getLatestMatchID(String account);

    /**
     * 根据比赛id获取比赛详情
     * @param matchID
     * @return
     */
    JSONObject getMatchDetail(String matchID);
}
