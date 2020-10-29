package com.yiqi.watcher.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Wang Hongyu
 * @title: Dota2Player
 * @projectName watcher-robot
 * @description: dota2玩家
 * @date 2020/10/27
 */
@Getter
@Setter
@ToString
public class Dota2Player extends Player {
    // 英雄id
    private Integer heroID;
    // 杀敌数
    private Integer kills;
    // 死亡数
    private Integer deaths;
    // 助攻数
    private Integer assists;
    // 补刀数
    private Integer lastHits;
    // 每分钟金钱
    private Integer gpm;
    // 每分钟经验
    private Integer xpm;
    // 英雄伤害
    private Integer heroDamage;
    // 阵营
    private Integer team;

    public Dota2Player(JSONObject player, String matchID) {
        super(player.getString("account_id"), "", matchID);
        this.heroID = player.getInteger("hero_id");
        this.kills = player.getInteger("kills");
        this.deaths = player.getInteger("deaths");
        this.assists = player.getInteger("assists");
        this.lastHits = player.getInteger("last_hits");
        this.gpm = player.getInteger("gold_per_min");
        this.xpm = player.getInteger("xp_per_min");
        this.heroDamage = player.getInteger("hero_damage");
        this.team = player.getInteger("player_slot") > 5 ? 2 : 1;
    }
}
