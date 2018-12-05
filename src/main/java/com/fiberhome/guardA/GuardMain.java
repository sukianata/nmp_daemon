package com.fiberhome.guardA;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 黃帆
 * @date 2018/10/26 12:58
 * @desc 该类的主要作用是检测wrapper是否启动，若未启动，则重启wrapper
 */
public class GuardMain{
    private static Logger logger= LogManager.getLogger(GuardMain.class);
    public static void main(String[] args) throws Exception {
        //初始化properties
        String confPath=args[0];
        if (confPath==null||confPath.isEmpty()){
            logger.error("config file path must be defined");
            return;
        }
        ConfigUtil.initialize(confPath);
        //开启一个线程 启动守护进程
        ScheduledExecutorService scheduledExecutorService= Executors.newScheduledThreadPool(1);
        MyTask task=new MyTask(Guard.getInstance());
        //运行结束后等待5秒。
        scheduledExecutorService.scheduleWithFixedDelay(task,0,Long.parseLong(ConfigUtil.getProperty("Interval")), TimeUnit.MILLISECONDS);
    }
}
