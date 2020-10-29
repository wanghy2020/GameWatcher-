package com.yiqi.watcher.entity;

import com.yiqi.watcher.service.QQRobotService;
import com.yiqi.watcher.service.QueryDota2MatchInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wang Hongyu
 * @title: Dota2Robot
 * @projectName watcher-robot
 * @description: Dota2战绩机器人
 * @date 2020/10/28
 */
@Component
public class Dota2Robot extends Robot {
    private static HashMap<String, Dota2Player> luckyGuys = new HashMap<>();
    @Autowired
    private QueryDota2MatchInfoService dota2MatchInfoService;
    @Autowired
    private QQRobotService qqRobotService;
    @Value("${steam.players}")
    private String players;
    @Value("${steam.dota2Robot.target}")
    String target;
    @Value("${steam.dota2Robot.qq}")
    String qq;
    @Value("${steam.dota2Robot.authKey}")
    String authKey;

    @Override
    public void run() {
        List<String[]> playersList = Arrays.stream(players.split(","))
                .map(_1 -> _1.split(":")).collect(Collectors.toList());
        playersList.forEach(i -> {
            String latestMatchID = dota2MatchInfoService.getLatestMatchID(i[1]);
            boolean flag = luckyGuys.containsKey(i[1]);
            if (!luckyGuys.containsKey(i[1]) || flag && !luckyGuys.get(i[1]).getLatestMatchID().equals(latestMatchID)) {
                Dota2Match dota2Match = new Dota2Match(dota2MatchInfoService.getMatchDetail(latestMatchID), i);
                luckyGuys.put(i[1], dota2Match.getLuckyGuy());
                if (flag){
                    String report = dota2Match.generateReport();
                    sendMessage(report);
                }
            }
        });
    }

    @Override
    public void sendMessage(String report) {
        qqRobotService.sendGroupMessage(authKey, qq, target, report);
    }
}
