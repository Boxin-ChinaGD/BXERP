package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.RetailTradeCommodity;
import wpos.model.RetailTradePromoting;

@Repository
public interface RetailTradePromotingMapper extends JpaRepository<RetailTradePromoting, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_TradeID INT NOT NULL," +
            "F_CreateDatetime DATETIME," +
            "F_SyncDatetime DATETIME," +
            "F_SyncType VARCHAR(1)" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "insert into T_RetailTradePromoting (F_ID, F_TradeID, F_CreateDatetime, F_SyncDatetime, F_SyncType) values " +
            "(:#{#retailTradePromoting.ID}, :#{#retailTradePromoting.tradeID}, :#{#retailTradePromoting.createDatetime}, :#{#retailTradePromoting.syncDatetime}, " +
            ":#{#retailTradePromoting.syncType});", nativeQuery = true)
    int create(@Param("retailTradePromoting") RetailTradePromoting retailTradePromoting);
}
