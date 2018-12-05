package com.fiberhome.guardA;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * @author 黃帆
 * @date 2018/10/26 12:54
 */
public class Guard {
    private static Logger logger= LogManager.getLogger(Guard.class);
    // GuardA用于维持自己的锁
    private File fileGuard;
    private FileOutputStream fileOutputStreamGuard;
    private FileChannel fileChannelGuard;
    private FileLock fileLockGuard;
    // GuardB用于检测Wrapper的锁
    private File fileGuardWrapper;
    private FileOutputStream fileOutputStreamGuardWrapper;
    private FileChannel fileChannelGuardWrapper;
    private FileLock fileLockGuardWrapper;

    private static String CURRDIR=System.getProperty("user.dir");

    private static volatile Guard instance;

    private Guard() throws Exception {
        File f=new File(FileUtil.dealPath(CURRDIR));
        String lockpath="";
        if (FileUtil.getSysName().contains("window")){
            lockpath=ConfigUtil.getProperty("WINDOWS_LOCKFILE_DIR");
        }
        if (FileUtil.getSysName().contains("linux")){
            lockpath=ConfigUtil.getProperty("LINUX_LOCKFILE_DIR");
        }
        File file=new File(lockpath);
        if (!file.exists()){
            logger.info(">>>>>create lock folder by daemon process,path:"+file.getAbsolutePath());
            boolean flag=file.mkdir();
            if(!flag){
                logger.error(">>>>failed to create lock folder ,path:"+file.getAbsolutePath());
            }
        }
        //获取自身的文件锁
        logger.info("daemon lock file>>"+FileUtil.dealPath(lockpath.concat(ConfigUtil.getProperty("DAEMON_LOCK_PATH"))));
        fileGuard = new File(FileUtil.dealPath(lockpath.concat(ConfigUtil.getProperty("DAEMON_LOCK_PATH"))));
        if (!fileGuard.exists()) {
            logger.info(">>>>>create daemon lock file,path:"+fileGuard.getAbsolutePath());
            boolean flag = fileGuard.createNewFile();
            if (!flag){
                logger.error(">>>>failed to create daemon lock file");
            }
        }
        //获取文件锁，拿不到则证明守护进程已启动
        fileOutputStreamGuard = new FileOutputStream(fileGuard);
        fileChannelGuard = fileOutputStreamGuard.getChannel();
        fileLockGuard = fileChannelGuard.tryLock();//trylock 方法在线程中无法获取锁
//        fileLockGuard =fileChannelGuard.lock();//lock方法无法获取锁时会一直阻塞
        if (fileLockGuard == null) {
          throw new RuntimeException("daemon process is already running");
        }
        logger.info("wrapper lock file>>"+FileUtil.dealPath(lockpath.concat(ConfigUtil.getProperty("WRAPPER_LOCK_PATH"))));
        fileGuardWrapper = new File(FileUtil.dealPath(lockpath.concat(ConfigUtil.getProperty("WRAPPER_LOCK_PATH"))));
        if (!fileGuardWrapper.exists()) {
            logger.info(">>>>>create nmosp_nmp lock file,path:"+fileGuardWrapper.getAbsolutePath());
            boolean flag = fileGuardWrapper.createNewFile();
            if (!flag){
                logger.error(">>>>failed to create nmosp_nmp lock file");
            }
        }
        fileOutputStreamGuardWrapper = new FileOutputStream(fileGuardWrapper);
        fileChannelGuardWrapper = fileOutputStreamGuardWrapper.getChannel();
    }

    public static Guard getInstance() {
        if (instance == null) {
            synchronized (Guard.class) {
                if (instance == null) {
                    try {
                        instance = new Guard();
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
            }
        }
        return instance;
    }


    /**
     * 检测 wrapper是否存在
     *
     * @return true B已经存在
     */
    public boolean checkWrapper() {
        try {
            logger.info(">>>>>check if wrapper service is running...");
            fileLockGuardWrapper = fileChannelGuardWrapper.tryLock();
            if (fileLockGuardWrapper == null) {
                return true;
            } else {
                fileLockGuardWrapper.release();
                return false;
            }
        } catch (IOException e) {
            // never touch
            return true;
        }
    }

    public void startNmp(){
        String cmd="";
        if (FileUtil.getSysName().contains("window")){
            String base_path=new File("").getAbsolutePath().replace("bin","");
            cmd =base_path+ConfigUtil.getProperty("START_NMOSP_NMP_WIN").replace("${base_path}",base_path);
        }
        if (FileUtil.getSysName().contains("linux")){
            cmd=ConfigUtil.getProperty("START_NMOSP_NMP_LINUX");
        }
        try {
            boolean flag = CmdUtil.execute(cmd);
            if (flag){
                logger.info("start nmosp_nmp service successfully!");
            }else{
                logger.error(">>>errors happended while starting nmosp_nmp preocess:");
            }
        } catch (InterruptedException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void stopNmp(){
        String cmd="";
        if (FileUtil.getSysName().contains("window")){
            String base_path=new File("").getAbsolutePath().replace("bin","");
            cmd =base_path+ConfigUtil.getProperty("STOP_NMOSP_NMP_WIN").replace("${base_path}",base_path);;
        }
        if (FileUtil.getSysName().contains("linux")){
            cmd=ConfigUtil.getProperty("STOP_NMOSP_NMP_LINUX");
        }
        try {
            boolean flag = CmdUtil.execute(cmd);
            if (flag){
                logger.info("stop nmosp_nmp service successfully!");
            }else{
                logger.error(">>>errors happended while stopping nmosp_nmp preocess:");
            }
        } catch (InterruptedException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }

    }
    public void restartNmp(){
        if (!this.checkWrapper()){
            this.startNmp();
        }
    }
}
