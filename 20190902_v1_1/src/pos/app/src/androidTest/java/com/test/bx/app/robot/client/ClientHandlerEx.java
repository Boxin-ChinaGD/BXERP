package com.test.bx.app.robot.client;


import com.bx.erp.utils.Shared;
import com.test.bx.app.robot.Config;
import com.test.bx.app.robot.ConfigEx;
import com.test.bx.app.robot.ShopRobotTest;
import com.test.bx.app.robot.protocol.Header;
import com.test.bx.app.robot.protocol.HeaderEx;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;

public class ClientHandlerEx implements IoHandler {

    private boolean connectedToServer = false;

    public static volatile boolean canRunNow = false;

    public static boolean bLogOut = false;

    private boolean closeConnect = false;

    public boolean isCanRunNow() {
        return canRunNow;
    }

    public synchronized void setCanRunNow(boolean canRunNow) {
        this.canRunNow = canRunNow;
    }

    public synchronized boolean isConnectedToServer() {
        return connectedToServer;
    }

    public boolean isCloseConnect() {
        return closeConnect;
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        connectedToServer = true;
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {

    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        StringBuffer buffer = new StringBuffer(ConfigEx.MAX_BodyLength);
        System.out.println(message.toString());
        buffer.append(message.toString());
        if (buffer.length() >= ConfigEx.HEADER_Length) {
            String tmp = buffer.substring(0, ConfigEx.commandLength);
            int command = Integer.parseInt(tmp.trim());
            if (command == HeaderEx.EnumCommandType.ECT_Run.getIndex()) {
                ShopRobotTest.caseLog("pos run");
                canRunNow = true;
            } else if (command == HeaderEx.EnumCommandType.ECT_Stop.getIndex()) {
                ShopRobotTest.caseLog("pos stop");
                closeConnect = true;
            } else if (command == HeaderEx.EnumCommandType.ECT_Logout.getIndex()) {
                ShopRobotTest.caseLog("pos logout");
                bLogOut = true;
            }
        } else {
            throw new RuntimeException("不合法的长度");
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {

    }

    @Override
    public void inputClosed(IoSession session) throws Exception {

    }

    @Override
    public void event(IoSession session, FilterEvent event) throws Exception {

    }
}
