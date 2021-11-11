package com.test.bx.app.robot.client;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import com.test.bx.app.robot.Config;
import com.test.bx.app.robot.MinaClientTest;
import com.test.bx.app.robot.ShopRobotTest;
import com.test.bx.app.robot.program.Program;
import com.test.bx.app.robot.protocol.Header;

// 客户端逻辑处理类ClientHandler：
public class ClientHandler implements IoHandler {
//	protected StringBuffer buffer = new StringBuffer(Config.MAX_BodyLength);

    public static  boolean timeToCloseConnected = false;

    public static volatile  String[] bodyInfoForCreateRt = null;

    @Override
    public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
        System.out.println("客户端exceptionCaught被调用！");
        throwable.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        System.out.println("客户端messageReceived被调用！");
        System.out.println("client端接收信息：" + message.toString());
        //
        StringBuffer buffer = new StringBuffer(Config.MAX_BodyLength);
        buffer.append(message.toString());
        int length = buffer.length();
        System.out.println(length);
        if (buffer.length() >= Config.HEADER_Length) {
            String tmp = buffer.substring(0, 4);
            int command = Integer.parseInt(tmp.trim());
            String activitySequenceStr = buffer.substring(4, 8).trim();
            int activitySequence = Integer.parseInt(activitySequenceStr);
            ShopRobotTest.activitySequence = activitySequence;
            tmp = buffer.substring(Config.HEADER_Length - 6, Config.HEADER_Length);
            int bodyLength = Integer.parseInt(tmp.trim());
            if (bodyLength > Config.MAX_BodyLength) {
                session.closeNow();
                return;
            }
            if (buffer.length() >= Config.HEADER_Length + bodyLength) {
                String json = buffer.substring(Config.HEADER_Length, Config.HEADER_Length + bodyLength);
                System.out.println(json);
            }
            if (command == Header.EnumCommandType.ECT_PosLogin.getIndex()) {
                System.out.println("Pos Login");
            } else if (command == Header.EnumCommandType.ECT_SyncData.getIndex()) {
                System.out.println("Pos SyncData");
                MinaClientTest.bCanRunSyncProgram = true;
            } else if (command == Header.EnumCommandType.ECT_CreateRetailTrade.getIndex()) {
                System.out.println("Pos Create RetailTrade");
                String dataFromServerToClient = buffer.substring(12, 26);
                Program.maxCommodityID = Integer.parseInt(dataFromServerToClient.split(",")[0].trim());
                Program.maxBarcodeID = Integer.parseInt(dataFromServerToClient.split(",")[1]);
                Program.maxRetailTradeID = Integer.parseInt(dataFromServerToClient.split(",")[2]);
                // 解析body
                String json = buffer.substring(Config.HEADER_Length, Config.HEADER_Length + bodyLength);
                bodyInfoForCreateRt = json.split(";");
                System.out.println(bodyInfoForCreateRt);
            } else if (command == Header.EnumCommandType.ECT_RunProgram.getIndex()) {
                System.out.println("Pos begin RunProgram");
                MinaClientTest.bStartRunProgram = true;
                ShopRobotTest.bProgramIsRunning = true;
            } else if (command == Header.EnumCommandType.ECT_CloseConnected.getIndex()) {
                System.out.println("Pos CloseConnected");
                timeToCloseConnected = true;
            }else {
                throw new RuntimeException("unknown command");
            }
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        System.out.println("客户端messageSent被调用！");
        System.out.println("client端发送信息：" + message.toString());
    }

    public void inputClosed(IoSession session) throws Exception {
        System.out.println("客户端inputClosed被调用！");
        System.out.println("client端：" + session.getId() + " 关闭输入");
        session.closeNow();
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("客户端sessionClosed被调用！");
        System.out.println("client端与：" + session.getRemoteAddress().toString() + " 关闭连接");
//        System.exit(0);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("客户端sessionCreated被调用！");
        System.out.println("client端与：" + session.getRemoteAddress().toString() + " 建立连接");
        MinaClientTest.clientSession = session;
        ShopRobotTest.clientSession = session;
        MinaClientTest.bCanRunLoginProgram = true;
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("客户端sessionIdle被调用！");
        System.out.println("client端闲置连接：会话 " + session.getId() + " 被触发 " + session.getIdleCount(status) + " 次");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("客户端sessionOpened被调用！");
        System.out.println("client端打开连接");
    }

    @Override
    public void event(IoSession session, FilterEvent event) throws Exception {
        // TODO Auto-generated method stub

    }
}