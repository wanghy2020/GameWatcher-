package com.yiqi.watcher.entity;

import com.alibaba.fastjson.JSONObject;
import com.yiqi.watcher.base.dota2.Common;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wang Hongyu
 * @title: Dota2Match
 * @projectName watcher-robot
 * @description: Dota2比赛详情类
 * @date 2020/10/28
 */
@Getter
@Setter
@ToString
public class Dota2Match extends Match {
    private static Common common = Enum.valueOf(Common.class, "Dota2");
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static String modal = "{0}{1}\n" +
            "开始时间: {2}\n" +
            "持续时间: {3}\n" +
            "游戏模式: {4}\n" +
            "{5}使用{6}, KDA: {7}, GPM/XPM: {8}, 补刀数: {9}, 总伤害: {10}, 参战率: {11}, 参葬率: {12}\n" +
            "战绩详情: http://www.dotamax.com/match/detail/{13}";
    private Dota2Player luckyGuy;
    private List<Dota2Player> dota2Players;
    private String matchID;
    private boolean win;
    private String startTime;
    private String duration;
    private String gameMode;

    @Override
    String generateReport() {
        String luckyGuyName = luckyGuy.getPlayerName();
        if ((win && luckyGuy.getTeam() == 1) || (!win && luckyGuy.getTeam() == 2)) {
            setWin(true);
        } else {
            setWin(false);
        }
        Long damageSum = dota2Players.stream().collect(Collectors.groupingBy(Dota2Player::getTeam,
                Collectors.summingLong(Dota2Player::getHeroDamage))).get(luckyGuy.getTeam());
        Long killerSum = dota2Players.stream().collect(Collectors.groupingBy(Dota2Player::getTeam,
                Collectors.summingLong(Dota2Player::getKills))).get(luckyGuy.getTeam());
        Long deathSum = dota2Players.stream().collect(Collectors.groupingBy(Dota2Player::getTeam,
                Collectors.summingLong(Dota2Player::getDeaths))).get(luckyGuy.getTeam());
        double kda = Math.floor((luckyGuy.getKills().floatValue() + luckyGuy.getAssists().floatValue()) /
                luckyGuy.getDeaths() * 10d) / 10;
        String canzhanRate = Math.floor((luckyGuy.getKills().doubleValue() + luckyGuy.getAssists().doubleValue()) / killerSum * 100 * 10d) / 10 + "%";
        String canzangRate = Math.floor(luckyGuy.getDeaths().doubleValue() / deathSum * 100 * 10d) / 10 + "%";
        boolean positive = kda > 6;
        String demage = MessageFormat.format("{0}({1}%)", luckyGuy.getHeroDamage(),
                Math.floor(luckyGuy.getHeroDamage().doubleValue() / damageSum.doubleValue() * 100 * 10d) / 10);
        String kdaStr = MessageFormat.format("{0}[{1}/{2}/{3}]", kda, luckyGuy.getKills(), luckyGuy.getDeaths(), luckyGuy.getAssists());
        String[] messageArray = judgeMessageArray(win, positive);
        String report = MessageFormat.format(modal, luckyGuyName, messageArray[(int) (Math.random() * messageArray.length)],
                startTime, duration, gameMode, luckyGuyName, common.getHEROES_LIST_CHINESE()[luckyGuy.getHeroID() - 1], kdaStr,
                luckyGuy.getGpm() + "/" + luckyGuy.getXpm(), luckyGuy.getLastHits(), demage, canzhanRate, canzangRate, matchID);
        return report;
    }

    public String[] judgeMessageArray(boolean win, boolean positive) {
        String[] messageArray = common.getWIN_POSTIVE_SOLO();
        if (win) {
            if (!positive)
                messageArray = common.getWIN_NEGATIVE_SOLO();
        } else {
            if (positive) {
                messageArray = common.getLOSE_POSTIVE_SOLO();
            } else {
                messageArray = common.getLOSE_NEGATIVE_SOLO();
            }
        }
        return messageArray;
    }

    public Dota2Match(JSONObject match, String[] luckGuy) {
        this.dota2Players = match.getJSONArray("players").stream().map(_1 -> new Dota2Player(JSONObject.parseObject(_1.toString()),
                match.getString("match_id"))).collect(Collectors.toList());
        this.luckyGuy = this.dota2Players.stream().filter(_1 -> _1.getPlayerID().equals(luckGuy[1])).collect(Collectors.toList()).get(0);
        this.luckyGuy.setPlayerName(luckGuy[0]);
        this.matchID = match.getString("match_id");
        this.startTime = simpleDateFormat.format(new Date(match.getLong("start_time") * 1000));
        this.win = match.getBoolean("radiant_win");
        this.duration = MessageFormat.format("{0}分{1}秒", match.getLong("duration") / 60,
                match.getLong("duration") % 60);
        this.gameMode = MessageFormat.format("[{0}/{1}]",
                common.getGAME_MODE()[match.getInteger("game_mode") - 1], common.getLOBBY()[match.getInteger("lobby_type") + 1]);
    }
}
