package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.RetailTradeCommodity;
import wpos.model.RetailTradePromotingFlow;

@Repository
public interface RetailTradePromotingFlowMapper extends JpaRepository<RetailTradePromotingFlow, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_RetailTradePromotingID INT NOT NULL," +
            "F_PromotionID INT NULL," +
            "F_ProcessFlow VARCHAR(2048) NOT NULL," +
            "F_CreateDatetime DATETIME," +
            "F_SyncDatetime DATETIME," +
            "F_SyncType VARCHAR(1)" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "insert into T_RetailTradePromotingFlow (F_ID, F_RetailTradePromotingID, F_PromotionID, F_ProcessFlow, F_CreateDatetime, F_SyncDatetime, F_SyncType) values" +
            "(:#{#retailTradePromotingFlow.ID}, :#{#retailTradePromotingFlow.retailTradePromotingID}, :#{#retailTradePromotingFlow.promotionID}, " +
            ":#{#retailTradePromotingFlow.processFlow}, :#{#retailTradePromotingFlow.createDatetime}, :#{#retailTradePromotingFlow.syncDatetime}, :#{#retailTradePromotingFlow.syncType});", nativeQuery = true)
    int create(@Param("retailTradePromotingFlow") RetailTradePromotingFlow retailTradePromotingFlow);
}
