package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.RetailTrade;

@Repository
public interface RetailTradeMapper extends JpaRepository<RetailTrade, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_VipID INT NULL," +
            "F_SN VARCHAR(26) NOT NULL," +
            "F_LocalSN INT NOT NULL," +
            "F_POS_ID INT NOT NULL," +
            "F_Logo VARCHAR(128) NOT NULL," +
            "F_SaleDatetime DATETIME NOT NULL," +
            "F_StaffID INT NOT NULL," +
            "F_PaymentType INT NOT NULL," +
            "F_PaymentAccount VARCHAR(20) NOT NULL," +
            "F_Status INT NOT NULL," +
            "F_Remark VARCHAR(20) NOT NULL," +
            "F_SourceID INT DEFAULT -1," +
            "F_SyncDatetime DATETIME NOT NULL," +
            "F_Amount Decimal(20,6) NOT NULL," +
            "F_AmountPaidIn Decimal(20,6) NOT NULL," +
            "F_AmountChange Decimal(20,6) NOT NULL," +
            "F_AmountCash Decimal(20,6) NULL," +
            "F_AmountAlipay Decimal(20,6) NULL," +
            "F_AmountWeChat Decimal(20,6) NULL," +
            "F_Amount1 Decimal(20,6) NULL," +
            "F_Amount2 Decimal(20,6) NULL," +
            "F_Amount3 Decimal(20,6) NULL," +
            "F_Amount4 Decimal(20,6) NULL," +
            "F_Amount5 Decimal(20,6) NULL," +
            "F_SmallSheetID INT NOT NULL," +
            "F_AliPayOrderSN VARCHAR(32) NULL," +
            "F_WxOrderSN VARCHAR(32) NULL," +
            "F_WxTradeNO VARCHAR(32) NULL," +
            "F_WxRefundNO VARCHAR(32) NULL," +
            "F_WxRefundDesc VARCHAR(80) NULL," +
            "F_WxRefundSubMchID VARCHAR(32) NULL," +
            "F_CouponAmount Decimal(20,6) NOT NULL DEFAULT '0.000000d'," +
            "F_ConsumerOpenID VARCHAR(100) NULL," +
            "F_SyncType VARCHAR(1)," +
            "F_SlaveCreated INT DEFAULT 0," +
            "F_ShopID INT NOT NULL" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "DROP TABLE T_RetailTrade;", nativeQuery = true)
    void dropTable();

    @Transactional
    @Modifying
    @Query(value = "insert into T_RetailTrade (F_ID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, " +
            "F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountPaidIn, F_AmountChange, F_AmountCash, F_AmountAlipay, " +
            "F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, " +
            "F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_CouponAmount, F_ConsumerOpenID, F_shopID) values" +
            "(:#{#retailTrade.ID}, :#{#retailTrade.vipID}, :#{#retailTrade.sn}, :#{#retailTrade.localSN}, :#{#retailTrade.pos_ID}, " +
            ":#{#retailTrade.logo}, :#{#retailTrade.saleDatetime}, :#{#retailTrade.staffID}, :#{#retailTrade.paymentType}, " +
            ":#{#retailTrade.paymentAccount}, :#{#retailTrade.status}, :#{#retailTrade.remark}, :#{#retailTrade.sourceID}, " +
            ":#{#retailTrade.syncDatetime}, :#{#retailTrade.amount}, :#{#retailTrade.amountPaidIn}, :#{#retailTrade.amountChange}, :#{#retailTrade.amountCash}, :#{#retailTrade.amountAlipay}, " +
            ":#{#retailTrade.amountWeChat}, :#{#retailTrade.amount1}, :#{#retailTrade.amount2}, :#{#retailTrade.amount3}, " +
            ":#{#retailTrade.amount4}, :#{#retailTrade.amount5}, :#{#retailTrade.smallSheetID}, :#{#retailTrade.aliPayOrderSN}, " +
            ":#{#retailTrade.wxOrderSN}, :#{#retailTrade.wxTradeNO}, :#{#retailTrade.wxRefundNO}, :#{#retailTrade.wxRefundDesc}, " +
            ":#{#retailTrade.wxRefundSubMchID}, :#{#retailTrade.couponAmount}, :#{#retailTrade.consumerOpenID}, :#{#retailTrade.shopID});", nativeQuery = true)
    void create(@Param("retailTrade") RetailTrade retailTrade);
}
