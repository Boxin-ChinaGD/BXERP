package com.test.bx.app.robot.client;


import com.test.bx.app.robot.Config;
import com.test.bx.app.robot.ConfigEx;
import com.test.bx.app.robot.Robot;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class RobotClientEx {

    private IoSession session;

    private ClientHandlerEx clientHandlerEx;

    public RobotClientEx() {
        clientHandlerEx = new ClientHandlerEx();
    }

    public ClientHandlerEx getClientHandlerEx() {
        return clientHandlerEx;
    }

    public void setClientHandlerEx(ClientHandlerEx clientHandlerEx) {
        this.clientHandlerEx = clientHandlerEx;
    }

    public IoSession getSession() {
        return session;
    }

    public void startClient() {
        // 创建客户端连接
        IoConnector connector = new NioSocketConnector();
        // 增加日志过滤器
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        // 增加编码过滤器，统一编码UTF-8
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PrefixedStringCodecFactory(Charset.forName("UTF-8"))));
        // 设置客户端逻辑处理器
        connector.setHandler(clientHandlerEx);
        // 连接
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(ConfigEx.SERVER_Address, Config.PORT));
        // 等待建立连接
        connectFuture.awaitUninterruptibly();
        // 获取连接会话
        session = connectFuture.getSession();
    }

    public void closeSession() {
        System.out.println("关闭客户端session!");
        session.closeNow();
    }
}
