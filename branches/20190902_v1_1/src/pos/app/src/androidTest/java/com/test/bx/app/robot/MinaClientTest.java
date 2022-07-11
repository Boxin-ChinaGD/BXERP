package com.test.bx.app.robot;


import com.bx.erp.helper.Constants;
import com.test.bx.app.robot.client.RobotClient;
import com.test.bx.app.robot.program.Program;
import com.test.bx.app.robot.protocol.Header;

import org.apache.mina.core.session.IoSession;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

//TODO 应该在本地启动一个服务器，客户端连接本地的服务器
public class MinaClientTest {
    public static boolean bStopClient = false;
    public static volatile boolean bStartRunProgram = false;
    public static IoSession clientSession;
    public static volatile boolean bCanRunLoginProgram = false;
    public static volatile boolean bCanRunSyncProgram = false;
    public static volatile boolean bCanRunRetailTradeProgram = false;
    public static boolean bIsDone = false;
    private static SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default2);
    protected static Queue<String> queueInLogin = new LinkedList<String>();
    protected static Queue<String> queueInSyncData = new LinkedList<String>();
    protected static Queue<String> queueInRetailTrade = new LinkedList<String>();
    @Test(timeout = 20000)
    public void test() {
        System.out.println("testStart");
        Thread tc = new Thread() {
            public void run() {
                RobotClient minaClient = new RobotClient();
                minaClient.init();
                while (true) {
                    if (bStopClient) {
                        break;
                    }
                }
            }
        };
        tc.start();
        while(true) {
            if(bStartRunProgram) {
                System.out.println("------客户端开始执行机器餐！-------");
                System.out.println("pos机器人拿到新建的商品ID为：" + Program.maxCommodityID);
                queueInLogin.add("a1");
                queueInSyncData.add("b1");
                queueInRetailTrade.add("c1");
                //
                if(run()) {

                }
                //
                Header header = new Header();
                header.setCommand(Header.EnumCommandType.ECT_PosCloseConnection.getIndex());
                header.setBodyLength(0);
                StringBuilder sb = new StringBuilder();
                sb.append(header.toBufferString());
                clientSession.write(sb.toString());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("关闭客户端");
                clientSession.closeNow();
                break;
            }
        }
    }

    public static boolean run() {
        while(!bIsDone) {
            if(bCanRunLoginProgram && !queueInLogin.isEmpty()) {
                String a = queueInLogin.poll();
                System.out.println("Pos进行登录：时间：" + sdf.format(new Date()));
                bCanRunLoginProgram = false;
                Header header = new Header();
                header.setCommand(Header.EnumCommandType.ECT_DonePosLogin.getIndex());
                header.setBodyLength(0);
                StringBuilder sb = new StringBuilder();
                sb.append(header.toBufferString());
                clientSession.write(sb.toString());
            }
            if(bCanRunSyncProgram && !queueInSyncData.isEmpty()) {
                String b = queueInSyncData.poll();
                System.out.println("Pos进行同步：时间：" + sdf.format(new Date()));
                bCanRunSyncProgram = false;
                bCanRunRetailTradeProgram = true;
            }
            if(bCanRunRetailTradeProgram && !queueInRetailTrade.isEmpty()) {
                String c = queueInRetailTrade.poll();
                System.out.println("Pos进行零售：时间：" + sdf.format(new Date()));
                bCanRunRetailTradeProgram = false;
                Header header = new Header();
                header.setCommand(Header.EnumCommandType.ECT_DoneCreateRetailTrade.getIndex());
                header.setBodyLength(0);
                StringBuilder sb = new StringBuilder();
                sb.append(header.toBufferString());
                clientSession.write(sb.toString());
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                bIsDone = true;
                queueInSyncData.add("a2");
            }
        }
        return true;
    }
}
