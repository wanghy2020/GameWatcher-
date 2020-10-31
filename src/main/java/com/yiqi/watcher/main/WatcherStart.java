package com.yiqi.watcher.main;

import com.yiqi.watcher.entity.Dota2Robot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Wang Hongyu
 * @title: WatcherStart
 * @projectName watcher-robot
 * @description: 机器人启动类
 * @date 2020/10/28
 */
@Component
public class WatcherStart implements CommandLineRunner {
    @Autowired
    private Dota2Robot dota2Robot;

    @Override
    public void run(String... args) throws Exception {
        new Thread(()->{
            while (true){
                dota2Robot.run();
                try {
                    Thread.sleep(1000 * 60 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
