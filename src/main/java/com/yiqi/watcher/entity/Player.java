package com.yiqi.watcher.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Wang Hongyu
 * @title: player
 * @projectName watcher-robot
 * @description: 玩家基本类
 * @date 2020/10/27
 */
@Getter
@Setter
@ToString
public abstract class Player implements Serializable {
    private static final long serialVersionUID = 131137940181031572L;

    protected String playerID = "";
    protected String playerName = "";
    protected String latestMatchID = "";

    public Player(String playerID, String playerName, String latestMatchID) {
        this.playerID = playerID;
        this.playerName = playerName;
        this.latestMatchID = latestMatchID;
    }


}
