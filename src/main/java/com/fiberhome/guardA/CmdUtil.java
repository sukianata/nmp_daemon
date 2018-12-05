package com.fiberhome.guardA;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CmdUtil {
    private static Logger logger= LogManager.getLogger(CmdUtil.class);

    public static boolean execute(String cmd) throws InterruptedException, IOException {
        logger.info("execute cmd:"+cmd);
        boolean flag=false;
         /*
          * 第三方工具包
          */
        CommandLine commandLine=CommandLine.parse(cmd);
        DefaultExecutor exec = new DefaultExecutor();
        //接收正常结果流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //接收异常结果流
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        //设置一分钟超时
        ExecuteWatchdog watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
        DefaultExecuteResultHandler resultHandler=new DefaultExecuteResultHandler();
        try {
            exec.setExitValue(1);
            exec.setWatchdog(watchdog);
            exec.execute(commandLine,resultHandler);
            /*
             * 下面的代码不会阻塞
             */
            //等待3秒，若不设置时间，则当前线程会一直阻塞
            resultHandler.waitFor(3000);
            flag=true;
            if (errorStream.size()>0){
                flag=false;
                logger.error("error happened while executing '"+cmd+"'");
            }
        } catch (IOException e) {
            logger.error(e);
            throw e;
        } catch (InterruptedException e) {
            logger.error(e);
            throw e;
        } finally {
            try {
                outputStream.close();
                errorStream.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return  flag;
    }
}
