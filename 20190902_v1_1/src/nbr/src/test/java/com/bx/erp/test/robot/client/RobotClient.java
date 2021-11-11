package com.bx.erp.test.robot.client;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.bx.erp.test.robot.Config;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class RobotClient { //extends Thread {
	protected IoSession session;

	public void init() {
		// 创建客户端连接
		IoConnector connector = new NioSocketConnector();
		// 增加日志过滤器
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		// 增加编码过滤器，统一编码UTF-8
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PrefixedStringCodecFactory(Charset.forName("UTF-8"))));
		// 设置客户端逻辑处理器
		connector.setHandler(new ClientHandler());
		connector.getSessionConfig().setReadBufferSize(1024);
		// 连接
		ConnectFuture connectFuture = connector.connect(new InetSocketAddress(Config.SERVER_Address, Config.PORT));
		// 等待建立连接
		connectFuture.awaitUninterruptibly();
		// 获取连接会话
		session = connectFuture.getSession();
		// 等待输入
//		Scanner sc = new Scanner(System.in);
//		System.out.println("客户端内容输入：");
//		boolean exit = false;
//		// 输入exit，退出系统
//		while (!exit) {
//			String str = sc.next();
//			session.write(str);
//			if (str.equalsIgnoreCase("exit")) {
//				exit = true;
//				session.closeNow();//.close(true);
//			}
//		}
	}

	public void send(String s) {
		session.write(s);
	}
}