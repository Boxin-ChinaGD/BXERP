package com.bx.erp.test.robot.client;


import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;

import com.bx.erp.test.robot.Config;
import com.bx.erp.test.robot.protocol.Header.EnumCommandType;

// 客户端逻辑处理类ClientHandler：
public class ClientHandler implements IoHandler {
//	protected StringBuffer buffer = new StringBuffer(Config.MAX_BodyLength);

	@Override
	public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
		System.out.println("客户端exceptionCaught被调用！");
		throwable.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println("客户端messageReceived被调用！");
		System.out.println("client端接收信息：" + message.toString());

		StringBuffer buffer = new StringBuffer(Config.MAX_BodyLength);
		buffer.append(message.toString());
		if (buffer.length() >= Config.HEADER_Length) {
			String tmp = buffer.substring(0, 4);
			int command = Integer.parseInt(tmp.trim());
			System.out.println(command);
			tmp = buffer.substring(4, Config.HEADER_Length);
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
			}
			// switch(EnumCommandType.values()[command]) {
			// case ECT_PosLogin:
			// System.out.println("Pos Login");
			// session.write("finishedLogin");
			// break;
			// case ECT_SyncData:
			// System.out.println("Pos SyncData");
			// session.write("finishedSyncData");
			// break;
			// case ECT_CreateRetailTrade:
			// System.out.println("Pos Create RetailTrade");
			// session.write("finishedCreateRetailTrade");
			// break;
			// default:
			// System.out.println("未知命令");
			// session.write("未知命令");
			// break;
			// }
			if (command == EnumCommandType.ECT_PosLogin.getIndex()) {
				System.out.println("Pos Login");
				session.write("finishedLogin");
			} else if (command == EnumCommandType.ECT_SyncData.getIndex()) {
				System.out.println("Pos SyncData");
				session.write("finishedSyncData");
			} else if (command == EnumCommandType.ECT_CreateRetailTrade.getIndex()) {
				System.out.println("Pos Create RetailTrade");
				session.write("finishedCreateRetailTrade");
			} else {
				System.out.println("未知命令");
				session.write("未知命令");
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
		System.exit(0);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("客户端sessionCreated被调用！");
		System.out.println("client端与：" + session.getRemoteAddress().toString() + " 建立连接");
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