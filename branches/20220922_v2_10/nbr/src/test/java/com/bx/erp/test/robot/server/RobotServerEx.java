package com.bx.erp.test.robot.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.bx.erp.test.robot.ConfigEx;
import com.bx.erp.test.robot.protocol.HeaderEx;

public class RobotServerEx {

	private static IoAcceptor acceptor;

	protected ServerHandlerEx serverHandlerEx;

	public RobotServerEx() {
		serverHandlerEx = new ServerHandlerEx();
	}

	public ServerHandlerEx getServerHandlerEx() {
		return serverHandlerEx;
	}

	public void startServer() {
		// 创建服务监听器，3是IoProcessor的线程数
		acceptor = new NioSocketAcceptor(3);
		// 增加日志过滤器
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		// 增加编码过滤器，统一编码UTF-8
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PrefixedStringCodecFactory(Charset.forName("UTF-8"))));
		// 设置服务端逻辑处理器
		acceptor.setHandler(serverHandlerEx);
		// 设置读缓存大小
		acceptor.getSessionConfig().setReadBufferSize(ConfigEx.SERVER_ReadBufferSize);
		// 设置指定类型的空闲时间，空闲时间超过这个值将触发sessionIdle方法
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, ConfigEx.SERVER_IdleTime);
		// 绑定端口
		try {
			acceptor.bind(new InetSocketAddress(ConfigEx.PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getClientCount() {
		return serverHandlerEx.getMapIoSession().size();
	}

	public void sendMessageToPosForRun() {
		HeaderEx headerEx = new HeaderEx();
		headerEx.setCommand(HeaderEx.EnumCommandType.ECT_Run.getIndex());
		headerEx.setBodyLength(0);
		StringBuilder sb = new StringBuilder();
		sb.append(headerEx.toBufferString());
		for (IoSession session : serverHandlerEx.getMapIoSession().values()) {
			session.write(sb.toString());
		}
	}
	
	public void sendMessageToPosForStop() {
		HeaderEx headerEx = new HeaderEx();
		headerEx.setCommand(HeaderEx.EnumCommandType.ECT_Stop.getIndex());
		headerEx.setBodyLength(0);
		StringBuilder sb = new StringBuilder();
		sb.append(headerEx.toBufferString());
		for (IoSession session : serverHandlerEx.getMapIoSession().values()) {
			session.write(sb.toString());
		}
	}

	public void sendMessageToPosForLogout() {
		HeaderEx headerEx = new HeaderEx();
		headerEx.setCommand(HeaderEx.EnumCommandType.ECT_Logout.getIndex());
		headerEx.setBodyLength(0);
		StringBuilder sb = new StringBuilder();
		sb.append(headerEx.toBufferString());
		for (IoSession session : serverHandlerEx.getMapIoSession().values()) {
			session.write(sb.toString());
		}
	}
}
