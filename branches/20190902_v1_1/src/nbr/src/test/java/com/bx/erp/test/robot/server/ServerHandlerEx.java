package com.bx.erp.test.robot.server;

import java.util.HashMap;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;

import com.bx.erp.test.Shared;
import com.bx.erp.test.robot.Config;
import com.bx.erp.test.robot.ConfigEx;
import com.bx.erp.test.robot.protocol.HeaderEx;

public class ServerHandlerEx implements IoHandler {

	protected volatile HashMap<Long, IoSession> mapIoSession = new HashMap<>();

	public static volatile boolean posDoneRun = false;

	public boolean isPosDoneRun() {
		return posDoneRun;
	}

	public void setPosDoneRun(boolean posDoneRun) {
		ServerHandlerEx.posDoneRun = posDoneRun;
	}

	public HashMap<Long, IoSession> getMapIoSession() {
		return mapIoSession;
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		mapIoSession.put(session.getId(), session);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		mapIoSession.remove(session.getId());
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println(message);
		StringBuffer buffer = new StringBuffer(Config.MAX_BodyLength);
		buffer.append(message.toString());
		if (buffer.length() >= ConfigEx.HEADER_Length) {
			String tmp = buffer.substring(0, 4);
			int command = Integer.parseInt(tmp.trim());
			if (command == HeaderEx.EnumCommandType.ECT_DoneRun.getIndex()) {
				Shared.caseLog("pos doneRun");
				posDoneRun = true;
			} else if (command == HeaderEx.EnumCommandType.ECT_DoneLogout.getIndex()) {
				Shared.caseLog("pos doneLogout");
				posDoneRun = true;
			}
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {

	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		session.closeNow();
	}

	@Override
	public void event(IoSession session, FilterEvent event) throws Exception {

	}

}
