package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.RetailTradeAggregation;

@Repository
public interface RetailTradeAggregationMapper extends JpaRepository<RetailTradeAggregation, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_StaffID INT NOT NULL," +
            "F_PosID INT NOT NULL," +
            "F_WorkTimeStart DATETIME NOT NULL," +
            "F_WorkTimeEnd DATETIME NULL," +
            "F_TradeNO INT NOT NULL," +
            "F_Amount Decimal(20,6) NOT NULL," +
            "F_ReserveAmount Decimal(20,6) NOT NULL," +
            "F_CashAmount Decimal(20,6) NULL," +
            "F_WechatAmount Decimal(20,6) NULL," +
            "F_AlipayAmount Decimal(20,6) NULL," +
            "F_Amount1 Decimal(20,6) NULL," +
            "F_Amount2 Decimal(20,6) NULL," +
            "F_Amount3 Decimal(20,6) NULL," +
            "F_Amount4 Decimal(20,6) NULL," +
            "F_Amount5 Decimal(20,6) NULL," +
            "F_UploadDateTime DATETIME," +
            "F_SyncDatetime DATETIME," +
            "F_SyncType VARCHAR(1)" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "insert into T_RetailTradeAggregation (F_ID, F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, " +
            "F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime, F_SyncDatetime, F_SyncType) values " +
            "(:#{#retailTradeAggregation.ID}, :#{#retailTradeAggregation.staffID}, :#{#retailTradeAggregation.posID}, :#{#retailTradeAggregation.workTimeStart}, " +
            ":#{#retailTradeAggregation.workTimeEnd}, :#{#retailTradeAggregation.tradeNO}, :#{#retailTradeAggregation.amount}, :#{#retailTradeAggregation.reserveAmount}, " +
            ":#{#retailTradeAggregation.cashAmount}, :#{#retailTradeAggregation.wechatAmount}, :#{#retailTradeAggregation.alipayAmount}, :#{#retailTradeAggregation.amount1}, " +
            ":#{#retailTradeAggregation.amount2}, :#{#retailTradeAggregation.amount3}, :#{#retailTradeAggregation.amount4}, :#{#retailTradeAggregation.amount5}, " +
            ":#{#retailTradeAggregation.uploadDateTime}, :#{#retailTradeAggregation.syncDatetime}, :#{#retailTradeAggregation.syncType});", nativeQuery = true)
    int create(@Param("retailTradeAggregation") RetailTradeAggregation retailTradeAggregation);
}
