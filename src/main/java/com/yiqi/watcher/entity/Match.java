package com.yiqi.watcher.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Wang Hongyu
 * @title: Match
 * @projectName watcher-robot
 * @description: TODO
 * @date 2020/10/28
 */
@Getter
@Setter
@ToString
public abstract class Match implements Serializable {
    private static final long serialVersionUID = -1436823024831957156L;

    /**
     * 生成战报
     * @return
     */
    abstract String generateReport();


}
