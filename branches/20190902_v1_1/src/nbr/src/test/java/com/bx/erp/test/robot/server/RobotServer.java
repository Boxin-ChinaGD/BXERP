package com.bx.erp.test.robot.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.test.robot.Config;
import com.bx.erp.test.robot.Config.EnumRetailTradeOperationType;
import com.bx.erp.test.robot.Config.EnumXlsMealColumn;
import com.bx.erp.test.robot.Robot;
import com.bx.erp.test.robot.model.ProgramAttendee;
import com.bx.erp.test.robot.program.CreateCommodity;
import com.bx.erp.test.robot.program.Program;
import com.bx.erp.test.robot.program.ProgramUnit;
import com.bx.erp.test.robot.protocol.Header;
import com.bx.erp.test.robot.protocol.Header.EnumCommandType;

public class RobotServer extends Thread {
	private static IoAcceptor acceptor;
	protected ServerHandler serverHandler;

	public RobotServer() {
		serverHandler = new ServerHandler();
	}

	public void init() {
		// 创建服务监听器，3是IoProcessor的线程数
		acceptor = new NioSocketAcceptor(3);
		// 增加日志过滤器
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		// 增加编码过滤器，统一编码UTF-8
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PrefixedStringCodecFactory(Charset.forName("UTF-8"))));
		// 设置服务端逻辑处理器
		acceptor.setHandler(serverHandler);
		// 设置读缓存大小
		acceptor.getSessionConfig().setReadBufferSize(Config.SERVER_ReadBufferSize);
		// 设置指定类型的空闲时间，空闲时间超过这个值将触发sessionIdle方法
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, Config.SERVER_IdleTime);
		// 绑定端口
		try {
			acceptor.bind(new InetSocketAddress(Config.PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 群发。将消息发给所有客户端 */
	public void sendToAll(ProgramUnit pu) {
		ProgramAttendee pa = pu.getAttendee();
		HashMap<Long, IoSession> map = serverHandler.getMapIoSession();
		for (IoSession session : map.values()) {
			if (session.getAttribute(Config.SessionKEY_PosNAME).equals(Config.CLIENT_Name[0])) {
				if (pa.getPos1ProgramUnitID() != 0) {
					// 不同指令需要发送不同的数据给pos端，如零售单需要商品ID、条形码ID
					if (Config.Pos_Activity[0].equals(pa.getPos1MachineMealInfo().get(EnumXlsMealColumn.EB_SheetTableName.getIndex()))) {
						sendMsgToPosForLogin(pu, session);
					} else if (Config.Pos_Activity[1].equals(pa.getPos1MachineMealInfo().get(EnumXlsMealColumn.EB_SheetTableName.getIndex()))) {
						sendMsgToPosForSyncData(pu, session);
					} else if (Config.Pos_Activity[2].equals(pa.getPos1MachineMealInfo().get(EnumXlsMealColumn.EB_SheetTableName.getIndex()))) {
						sendMsgToPosForCreateRetailTrade(pu, session);
					}
				}
			} else if (session.getAttribute(Config.SessionKEY_PosNAME).equals(Config.CLIENT_Name[1])) {
				if (pa.getPos2ProgramUnitID() != 0) {
					// 不同指令需要发送不同的数据给pos端，如零售单需要商品ID、条形码ID
					if (Config.Pos_Activity[0].equals(pa.getPos2MachineMealInfo().get(EnumXlsMealColumn.EB_SheetTableName.getIndex()))) {
						sendMsgToPosForLogin(pu, session);
					} else if (Config.Pos_Activity[1].equals(pa.getPos2MachineMealInfo().get(EnumXlsMealColumn.EB_SheetTableName.getIndex()))) {
						sendMsgToPosForSyncData(pu, session);
					} else if (Config.Pos_Activity[2].equals(pa.getPos2MachineMealInfo().get(EnumXlsMealColumn.EB_SheetTableName.getIndex()))) {
						sendMsgToPosForCreateRetailTrade(pu, session);
					}
				}
			} else if (session.getAttribute(Config.SessionKEY_PosNAME).equals(Config.CLIENT_Name[2])) {
				if (pa.getPos3ProgramUnitID() != 0) {
					// 不同指令需要发送不同的数据给pos端，如零售单需要商品ID、条形码ID
					if (Config.Pos_Activity[0].equals(pa.getPos3MachineMealInfo().get(EnumXlsMealColumn.EB_SheetTableName.getIndex()))) {
						sendMsgToPosForLogin(pu, session);
					} else if (Config.Pos_Activity[1].equals(pa.getPos3MachineMealInfo().get(EnumXlsMealColumn.EB_SheetTableName.getIndex()))) {
						sendMsgToPosForSyncData(pu, session);
					} else if (Config.Pos_Activity[2].equals(pa.getPos3MachineMealInfo().get(EnumXlsMealColumn.EB_SheetTableName.getIndex()))) {
						sendMsgToPosForCreateRetailTrade(pu, session);
					}
				}
			}
		}
	}

	private void sendMsgToPosForCreateRetailTrade(ProgramUnit pu, IoSession session) {
		ProgramAttendee attendee = pu.getAttendee();
		// TODO 这里写死了pos3机器餐，因为是用pos3跑日报表零售数据的
		List<String> pos3MealInfoList = attendee.getPos3MachineMealInfo();
		int retailtradeOperationType = Integer.parseInt(pos3MealInfoList.get(EnumXlsMealColumn.EB_OperationType.getIndex()));
		// 判断本次活动是否是创建退货型零售单
		if(retailtradeOperationType == EnumRetailTradeOperationType.ERTOT_CreateReturnRetailTrade.getIndex()) {
			// 找到源单在excel表的ID
			// TODO 查找源单的活动序号，是否考虑优化
			String programNo = "";
			for (List<String> retailTradeInfoList : Robot.mapBaseModels.get("零售单主表").values()) {
//				System.out.println("退货单ID：" + strList.get(2));
				if(retailTradeInfoList.get(Config.EnumXlsMealColumn.EB_ProgramUnitNO.getIndex()).equals(pos3MealInfoList.get(EnumXlsMealColumn.EB_MasterTable.getIndex()))) {
					String sourceID = retailTradeInfoList.get(Config.EnumXlsRetailTradeMasterTableColumnName.ERTMTCN_sourceID.getIndex());
					programNo = searchProgramNoByRetailTradeSourceID(sourceID);
					break;
				}
			}
			if(programNo.equals("")) {
				throw new RuntimeException("创建退货型零售单时找不到源零售单活动序号");
			}
			Iterator<?> ite = Robot.queueOut.iterator();
			while(ite.hasNext()) {
				ProgramUnit pUnit = (ProgramUnit) ite.next();
				if(pUnit.getNo() == Integer.parseInt(programNo)) {
					BaseModel bm =  pUnit.getBaseModelIn1();
					Program.maxRetailTradeID = bm.getID();
					break;
				}
			}
		}
		// TODO 怎么把真实的商品ID和条形码ID传给pos，放到body？
		StringBuffer bodyInfoForCreateRetailTrade = new StringBuffer();
		String retailTradeIdInExcel = pos3MealInfoList.get(Config.EnumXlsMealColumn.EB_MasterTable.getIndex());
        for (List<String> rtComm : Robot.mapBaseModels.get("零售单从表").values()) {
            if (rtComm.get(Config.EnumXlsRetailTradeCommodityTableColumnName.ERTCTCN_tradeID.getIndex()).equals(retailTradeIdInExcel)) {
            	int commIdInExcel = Integer.parseInt(rtComm.get(Config.EnumXlsRetailTradeCommodityTableColumnName.ERTCTCN_commodityID.getIndex()));
            	int commIdInDB = Robot.findRealCommodityIdByExcelCommodityID(commIdInExcel);
            	Commodity commodity = new Commodity();
            	commodity.setID(commIdInDB);
            	List<Barcodes> barcodeList = CreateCommodity.retrieveNBarcodeEx(commodity);
            	int barcodeIdInDB = barcodeList.get(0).getID();
            	bodyInfoForCreateRetailTrade.append(commIdInDB + "," + barcodeIdInDB +";");
            }
        }
		//
		Header header = new Header();
		header.setDataFromServerToClient(String.valueOf(Program.maxCommodityID) + "," + (Program.maxBarcodesID) + "," + Program.maxRetailTradeID);
		header.setCommand(EnumCommandType.ECT_CreateRetailTrade.getIndex());
		header.setActivitySequence(pu.getNo());
		header.setBodyLength(bodyInfoForCreateRetailTrade.length());
		header.setPosName("1111"); //随便一个名字都可以
		StringBuilder sb = new StringBuilder();
		sb.append(header.toBufferString());
		sb.append(bodyInfoForCreateRetailTrade);
		session.write(sb.toString());
	}


	private void sendMsgToPosForSyncData(ProgramUnit pu, IoSession session) {
		Header header = new Header();
		header.setCommand(EnumCommandType.ECT_SyncData.getIndex());
		header.setActivitySequence(pu.getNo());
		header.setBodyLength(0);
		header.setPosName("1111");//随便一个名字都可以
		StringBuilder sb = new StringBuilder();
		sb.append(header.toBufferString());
		session.write(sb.toString());
	}

	private void sendMsgToPosForLogin(ProgramUnit pu, IoSession session) {
		Header header = new Header();
		header.setCommand(EnumCommandType.ECT_PosLogin.getIndex());
		header.setActivitySequence(pu.getNo());
		header.setBodyLength(0);
		header.setPosName("1111");//随便一个名字都可以
		StringBuilder sb = new StringBuilder();
		sb.append(header.toBufferString());
		session.write(sb.toString());
	}

	public int getClientCount() {
		return serverHandler.getMapIoSession().size();
	}

	public void sendMsgToPosForRun() {
		Header header = new Header();
		header.setCommand(EnumCommandType.ECT_RunProgram.getIndex());
		header.setActivitySequence(0);
		header.setBodyLength(0);
		header.setPosName("1111");//随便一个名字都可以
		StringBuilder sb = new StringBuilder();
		sb.append(header.toBufferString());
		for (IoSession session : serverHandler.getMapIoSession().values()) {
			session.write(sb.toString());
		}
	}
	
	public void sendMsgToPosForCloseConnect() {
		Header header = new Header();
		header.setCommand(EnumCommandType.ECT_CloseConnected.getIndex());
		header.setActivitySequence(1);
		header.setBodyLength(0);
		header.setPosName("1111");//随便一个名字都可以
		StringBuilder sb = new StringBuilder();
		sb.append(header.toBufferString());
		for (IoSession session : serverHandler.getMapIoSession().values()) {
			session.write(sb.toString());
		}
	}

	public boolean isPos1Eaten() {
		return serverHandler.isPos1Eaten();
	}

	public void setPos1Eaten(boolean pos1Eaten) {
		serverHandler.setPos1Eaten(pos1Eaten);
	}

	public boolean isPos2Eaten() {
		return serverHandler.isPos2Eaten();
	}

	public void setPos2Eaten(boolean pos2Eaten) {
		serverHandler.setPos2Eaten(pos2Eaten);
	}
	
	public boolean isPos3Eaten() {
		return serverHandler.isPos3Eaten();
	}

	public void setPos3Eaten(boolean pos3Eaten) {
		serverHandler.setPos3Eaten(pos3Eaten);
	}
	
	private String searchProgramNoByRetailTradeSourceID(String sourceID) {
		String activityNo = "";
		// 分别找pos1、pos2、pos3机器餐
		for (List<String> mealInfo : Robot.mapBaseModels.get("pos1机器餐").values()) {
			if(mealInfo.get(EnumXlsMealColumn.EB_MasterTable.getIndex()) == null) {
				continue;
			}
			if(mealInfo.get(EnumXlsMealColumn.EB_MasterTable.getIndex()).equals(sourceID)) {
				activityNo = mealInfo.get(EnumXlsMealColumn.EB_ProgramUnitNO.getIndex());
				break;
			}
		}
		for (List<String> mealInfo : Robot.mapBaseModels.get("pos2机器餐").values()) {
			if(mealInfo.get(EnumXlsMealColumn.EB_MasterTable.getIndex()) == null) {
				continue;
			}
			if(mealInfo.get(EnumXlsMealColumn.EB_MasterTable.getIndex()).equals(sourceID)) {
				activityNo = mealInfo.get(EnumXlsMealColumn.EB_ProgramUnitNO.getIndex());
				break;
			}
		}
		for (List<String> mealInfo : Robot.mapBaseModels.get("pos3机器餐").values()) {
			if(mealInfo.get(EnumXlsMealColumn.EB_MasterTable.getIndex()) == null) {
				continue;
			}
			if(mealInfo.get(EnumXlsMealColumn.EB_MasterTable.getIndex()).equals(sourceID)) {
				activityNo = mealInfo.get(EnumXlsMealColumn.EB_ProgramUnitNO.getIndex());
				break;
			}
		}
		return activityNo;
	}
}