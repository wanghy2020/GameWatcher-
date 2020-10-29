package com.yiqi.watcher.service;

import com.alibaba.fastjson.JSONObject;
import com.yiqi.watcher.utils.http.HttpGet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author Wang Hongyu
 * @title: QueryMatchInfoService
 * @projectName watcher-robot
 * @description: dota2比赛查询接口
 * @date 2020/10/28
 */
@Slf4j
@Component
public class QueryDota2MatchInfoService implements QueryMatchInfoIService {
    private static final long serialVersionUID = 5195123786441031881L;

    @Value("${steam.api_key}")
    private String steamApiKey;
    private final static String getLatestMatchIDBaseUrl = "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/v001/?key={0}&account_id={1}&matches_requested=1";
    private final static String getLatestMatchDetailBaseUrl = "https://api.steampowered.com/IDOTA2Match_570/GetMatchDetails/V001/??key={0}&match_id={1}";

    @Autowired
    private HttpGet httpGet;

    /**
     * 获取最新的比赛id
     * @return match_id
     */
    public String getLatestMatchID(String shortSteamID){
        String matchID = "";
        try {
            String url = MessageFormat.format(getLatestMatchIDBaseUrl,steamApiKey,shortSteamID);
            JSONObject httpResult = httpGet.sendRequest(url);
            JSONObject match = JSONObject.parseObject(httpResult.getJSONObject("result").getJSONArray("matches").get(0).toString());
            matchID = match.getString("match_id");
        }catch (Exception e){
            log.error("查询比赛ID报错，报错信息为{}",e.getMessage());
        }
        return matchID;
    }

    public JSONObject getMatchDetail(String matchID){
        JSONObject match = new JSONObject();
        try {
            String url = MessageFormat.format(getLatestMatchDetailBaseUrl,steamApiKey,matchID);
            JSONObject httpResult = httpGet.sendRequest(url);
            match = httpResult.getJSONObject("result");
        }catch (Exception e){
            log.error("查询比赛详情报错，报错信息为{}",e.getMessage());
        }
        return match;
    }
}
