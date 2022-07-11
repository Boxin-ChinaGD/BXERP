package com.bx.erp.test.robot.server;

import java.util.HashMap;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;

import com.bx.erp.test.robot.Config;
import com.bx.erp.test.robot.Robot;
import com.bx.erp.test.robot.program.Program;
import com.bx.erp.test.robot.program.ProgramUnit;
import com.bx.erp.test.robot.protocol.Header.EnumCommandType;

// 服务端逻辑处理类ServerHandler：
public class ServerHandler implements IoHandler {
	protected HashMap<Long, IoSession> mapIoSession = new HashMap<>();

	public static int startPosNum = 0;
	public static int stopPosNum = 0;

	public HashMap<Long, IoSession> getMapIoSession() {
		return mapIoSession;
	}

	/** 控制服务器的退出。设为true后，服务器将退出 */
	protected static volatile boolean timeToStopServer = false;

	protected boolean pos1Eaten = false;

	protected boolean pos2Eaten = false;

	protected boolean pos3Eaten = false;

	public boolean isPos1Eaten() {
		return pos1Eaten;
	}

	public void setPos1Eaten(boolean pos1Eaten) {
		this.pos1Eaten = pos1Eaten;
	}

	public boolean isPos2Eaten() {
		return pos2Eaten;
	}

	public void setPos2Eaten(boolean pos2Eaten) {
		this.pos2Eaten = pos2Eaten;
	}

	public boolean isPos3Eaten() {
		return pos3Eaten;
	}

	public void setPos3Eaten(boolean pos3Eaten) {
		this.pos3Eaten = pos3Eaten;
	}

	public static boolean isTimeToStopServer() {
		return timeToStopServer;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
		System.out.println("服务端exceptionCaught被调用！");
		throwable.printStackTrace();
	}

	/*
	*当服务端收到消息时该方法被调用，可以对收到的数据进行处理
	*/
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println("服务端messageReceived被调用！");
		String str = message.toString();
		System.out.println("server端接收到的消息：" + str);
		//
		StringBuffer buffer = new StringBuffer(Config.MAX_BodyLength);
		buffer.append(message.toString());
		if (buffer.length() >= Config.HEADER_Length) {
			String tmp = buffer.substring(0, 4);
			int command = Integer.parseInt(tmp.trim());
			System.out.println(command);
			tmp = buffer.substring(27, Config.HEADER_Length);
			int bodyLength = Integer.parseInt(tmp.trim());
			if (bodyLength > Config.MAX_BodyLength) {
				session.closeNow();
				return;
			}
			if (buffer.length() >= Config.HEADER_Length + bodyLength) {
				String json = buffer.substring(Config.HEADER_Length, Config.HEADER_Length + bodyLength);
				// ApprovePurchasingOrder apo = (ApprovePurchasingOrder) new
				// ApprovePurchasingOrder(new Date(), new Date(), 2).fromJson(json);
				// System.out.println(apo.toString());
				System.out.println(json);
			} else {
				throw new RuntimeException("包超长，需要找Giggs重构代码！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
			}
			handleClientRequest(buffer, command, session, bodyLength);
		}
	}

	private void handleClientRequest(StringBuffer buffer, int command, IoSession session, int bodyLength) {
		if (command == EnumCommandType.ECT_PosCloseConnection.getIndex()) {
			if (session.getAttribute(Config.SessionKEY_PosNAME).equals(Config.CLIENT_Name[0])) {
				pos1Eaten = true;
				System.out.println(" ---------- pos1 closeConnection, NO: " + buffer.substring(4, 8).trim() + " ---------- ");
			}
			if (session.getAttribute(Config.SessionKEY_PosNAME).equals(Config.CLIENT_Name[1])) {
				pos2Eaten = true;
				System.out.println(" ---------- pos2 closeConnection, NO: " + buffer.substring(4, 8).trim() + " ---------- ");
			}
			if (session.getAttribute(Config.SessionKEY_PosNAME).equals(Config.CLIENT_Name[2])) {
				pos3Eaten = true;
				System.out.println(" ---------- pos3 closeConnection, NO: " + buffer.substring(4, 8).trim() + " ---------- ");
			}
			//
			timeToStopServer = true;
			Program.currentProgramUnitNO = 100;
			stopPosNum++;
		} else if (command == EnumCommandType.ECT_DonePosLogin.getIndex()) {
			if (session.getAttribute(Config.SessionKEY_PosNAME).equals(Config.CLIENT_Name[0])) {
				pos1Eaten = true;
				addToQueueOut(buffer, bodyLength);
				System.out.println(" ---------- Pos1 DonePosLogin, NO: " + buffer.substring(4, 8).trim() + " ---------- ");
			}
			if (session.getAttribute(Config.SessionKEY_PosNAME).equals(Config.CLIENT_Name[1])) {
				pos2Eaten = true;
				addToQueueOut(buffer, bodyLength);
				System.out.println(" ---------- Pos2 DonePosLogin, NO: " + buffer.substring(4, 8).trim() + " ---------- ");
			}
			if (session.getAttribute(Config.SessionKEY_PosNAME).equals(Config.CLIENT_Name[2])) {
				pos3Eaten = true;
				addToQueueOut(buffer, bodyLength);
				System.out.println(" ---------- Pos3 DonePosLogin, NO: " + buffer.substring(4, 8).trim() + " ---------- ");
			}
			//
		} else if (command == EnumCommandType.ECT_DoneCreateRetailTrade.getIndex()) {
			if (session.getAttribute(Config.SessionKEY_PosNAME).equals(Config.CLIENT_Name[0])) {
				pos1Eaten = true;
				addToQueueOut(buffer, bodyLength);
				System.out.println(" ---------- Pos1 DoneCreateRetailTrade, NO: " + buffer.substring(4, 8).trim() + " ---------- ");
			}
			if (session.getAttribute(Config.SessionKEY_PosNAME).equals(Config.CLIENT_Name[1])) {
				pos2Eaten = true;
				addToQueueOut(buffer, bodyLength);
				System.out.println(" ---------- Pos2 DoneCreateRetailTrade, NO: " + buffer.substring(4, 8).trim() + " ---------- ");
			}
			if (session.getAttribute(Config.SessionKEY_PosNAME).equals(Config.CLIENT_Name[2])) {
				pos3Eaten = true;
				addToQueueOut(buffer, bodyLength);
				System.out.println(" ---------- Pos3 DoneCreateRetailTrade, NO: " + buffer.substring(4, 8).trim() + " ---------- ");
			}
			//
		} else if (command == EnumCommandType.ECT_DoneSyncData.getIndex()) {
			if (session.getAttribute(Config.SessionKEY_PosNAME).equals(Config.CLIENT_Name[0])) {
				pos1Eaten = true;
				addToQueueOut(buffer, bodyLength);
				System.out.println(" ---------- Pos1 DoneSyncData, NO: " + buffer.substring(4, 8).trim() + " ---------- ");
			}
			if (session.getAttribute(Config.SessionKEY_PosNAME).equals(Config.CLIENT_Name[1])) {
				pos2Eaten = true;
				addToQueueOut(buffer, bodyLength);
				System.out.println(" ---------- Pos2 DoneSyncData, NO: " + buffer.substring(4, 8).trim() + " ---------- ");
			}
			if (session.getAttribute(Config.SessionKEY_PosNAME).equals(Config.CLIENT_Name[2])) {
				pos3Eaten = true;
				addToQueueOut(buffer, bodyLength);
				System.out.println(" ---------- Pos3 DoneSyncData, NO: " + buffer.substring(4, 8).trim() + " ---------- ");
			}
			//
		} else if (command == EnumCommandType.ECT_DoneStartProgram.getIndex()) {
			String posName = buffer.substring(8, 12);
			System.out.println("posName:" + posName);
			session.setAttribute("posName", posName);
			startPosNum++;
			//
			System.out.println(" ---------- Pos DoneStartProgram, NO: " + buffer.substring(4, 8).trim() + " ---------- ");
		} else {
			throw new RuntimeException("unknown command");
		}
	}


	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		System.out.println("server端发送信息:" + message.toString());

	}

	public void inputClosed(IoSession session) throws Exception {
		System.out.println("服务端inputClosed被调用！");
		System.out.println("server端：" + session.getId() + " 关闭输入");
		session.closeNow();
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("服务端sessionClosed被调用！");
		System.out.println("server端IP：" + session.getRemoteAddress().toString() + " 关闭连接");
		// System.exit(0);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("服务端sessionCreated被调用！");
		System.out.println("client端IP：" + session.getRemoteAddress().toString() + " 创建连接");
		mapIoSession.put(session.getId(), session);
		// ...将来的代码是吃机器餐。执行完后，通知客户端吃机器餐

		// // 发送给客户端连接已建立
		// ApprovePurchasingOrder apo = new ApprovePurchasingOrder(new Date(), new
		// Date(), 2);
		// List<PurchasingOrder> purchasingOrderList = new ArrayList<>();
		// for(int i=0; i<10; i++) {
		// PurchasingOrder p = new PurchasingOrder();
		// purchasingOrderList.add(p);
		// }
		// apo.setPurchasingOrderList(purchasingOrderList);
		// String body = apo.toJson(apo);//... fast json apo->json
		// Header h = new Header();
		// h.setBodyLength(body.length());
		// StringBuilder sb = new StringBuilder();
		// sb.append(h.toBufferString());
		// sb.append(body);
		// session.write(sb.toString());
		// Header header = new Header();
		// header.setCommand(EnumCommandType.ECT_PosLogin.getIndex());
		// header.setBodyLength(0);
		// StringBuilder sb = new StringBuilder();
		// sb.append(header.toBufferString());
		// session.write(sb.toString());
//		ShopRobotTest.bClientConnected = true;
//		ShopRobotTest.clientSession = session;
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		System.out.println("服务端sessionIdle被调用！");
		System.out.println("server端闲置连接：会话 " + session.getId() + " 被触发 " + session.getIdleCount(status) + " 次");
	}

	/*
	*当有客户端连接上服务器时，该方法被调用
	*在本方法中调用数据发送的线程
	*/
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("服务端sessionOpened被调用！");
		System.out.println("server端打开连接");
	}

	public void event(IoSession session, FilterEvent event) throws Exception {
		// TODO Auto-generated method stub

	}
	

	private void addToQueueOut(StringBuffer buffer, int bodyLength) {
		// 解析body
		String json = buffer.substring(Config.HEADER_Length, Config.HEADER_Length + bodyLength);
		ProgramUnit pu = (ProgramUnit) new ProgramUnit().fromJson(json);
		Robot.queueOut.add(pu);
		System.out.println("queueOut.size:" + Robot.queueOut.size());
	}
}