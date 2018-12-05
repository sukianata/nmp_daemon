package com.fiberhome.guardA;

import com.fiberhome.thrift.NMPDataService;
import com.fiberhome.thrift.ProcStatus;
import com.fiberhome.thrift.RPCApplicationException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.List;
import java.util.TimerTask;

/**
 * @author 黃帆
 * @date 2018/10/31 15:22
 */
public class MyTask extends TimerTask {
    private static Logger logger= LogManager.getLogger(MyTask.class);
    private Guard guard ;
    public MyTask(Guard guard){
        this.guard=guard;
    }
    @Override
    public void run() {
        try {
                if (!guard.checkWrapper()) {
                    logger.info("java wrapper service is not running,will restart it");
                    //启动wrapper，wrapper启动变会启动A
                    //A启动的时候检测B是否启动，若未启动则启动B
                    //假如wrapper终止，B会启动wrapper
                    guard.restartNmp();
                }else{
                    /**
                     * 检测cpu使用率，若使用过高则重启
                     */
                    boolean flag=checkUseageOfCpu();
                    if (flag){
                        guard.stopNmp();//过段时间后会自动重启
                    }
                }
        } catch (Exception e) {
           logger.error(e);
        }
    }

    private boolean checkUseageOfCpu(){
        boolean rt=false;
        String ip = ConfigUtil.getProperty("IP");
        int port = Integer.parseInt(ConfigUtil.getProperty("PORT"));
        TTransport transport = null;
        try {
            TSocket tSocket = new TSocket(ip, port, 60000);
            transport = new TFramedTransport(tSocket, 524288000);
            TProtocol protocol = new TCompactProtocol(transport);
            NMPDataService.Client client = new NMPDataService.Client(protocol);
            transport.open();
            ProcStatus status=client.getCurProcSt();
            if (status.getProcCPU()>Double.parseDouble(ConfigUtil.getProperty("CPU_LEVEL"))){
                logger.info("the useage of cpu:"+status.getProcCPU());
                logger.info("restart nmosp nmp...");
                rt=true;
            }
            transport.close();
        }catch (TTransportException e) {
            logger.error(e);
        } catch (RPCApplicationException e) {
            logger.error(e);
        } catch (TException e) {
            logger.error(e);
        }
        return rt;
    }

    public static void main(String[] args) throws Exception {
        String ip = ConfigUtil.getProperty("IP");
        int port = Integer.parseInt(ConfigUtil.getProperty("PORT"));
        TTransport transport = null;
        try {
            TSocket tSocket = new TSocket("10.31.109.22", port, 60000);
            transport = new TFramedTransport(tSocket, 524288000);
            TProtocol protocol = new TCompactProtocol(transport);
            NMPDataService.Client client = new NMPDataService.Client(protocol);
            transport.open();
            ProcStatus status=client.getCurProcSt();
            if (status.getProcCPU()>Double.parseDouble(ConfigUtil.getProperty("CPU_LEVEL"))){
                System.out.println(status.getProcCPU());
            }
            transport.close();
        }catch (TTransportException e) {
            logger.error(e);
        } catch (RPCApplicationException e) {
            logger.error(e);
        } catch (TException e) {
            logger.error(e);
        }
    }
}
