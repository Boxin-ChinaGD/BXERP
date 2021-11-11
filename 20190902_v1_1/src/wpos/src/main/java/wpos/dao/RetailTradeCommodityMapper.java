package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.RetailTradeAggregation;
import wpos.model.RetailTradeCommodity;

@Repository
public interface RetailTradeCommodityMapper extends JpaRepository<RetailTradeCommodity, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_TradeID INT NOT NULL," +
            "F_CommodityID INT NULL ," +
            "F_CommodityName VARCHAR(32) NULL," +
            "F_BarcodeID INT NULL," +
            "F_NO INT NOT NULL," +
            "F_PriceOriginal Decimal(20,6) NULL," +
            "F_NOCanReturn INT NOT NULL," +
            "F_PriceReturn Decimal(20,6) NULL," +
            "F_PriceSpecialOffer Decimal(20,6) NULL," +
            "F_PriceVIPOriginal Decimal(20,6) NULL," +
            "F_SyncType VARCHAR(1)," +
            "F_SyncDatetime DATETIME," +
            "F_PromotionID INT" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "insert into T_RetailTradeCommodity (F_ID, F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, " +
            "F_PriceSpecialOffer, F_PriceVIPOriginal, F_SyncType, F_SyncDatetime, F_PromotionID) values (:#{#retailTradeCommodity.ID}, :#{#retailTradeCommodity.tradeID}, " +
            ":#{#retailTradeCommodity.commodityID}, :#{#retailTradeCommodity.commodityName}, :#{#retailTradeCommodity.barcodeID}, :#{#retailTradeCommodity.NO}, " +
            ":#{#retailTradeCommodity.priceOriginal}, :#{#retailTradeCommodity.NOCanReturn}, :#{#retailTradeCommodity.priceReturn}, :#{#retailTradeCommodity.priceSpecialOffer}, " +
            ":#{#retailTradeCommodity.priceVIPOriginal}, :#{#retailTradeCommodity.syncType}, :#{#retailTradeCommodity.syncDatetime}, :#{#retailTradeCommodity.promotionID});", nativeQuery = true)
    int create(@Param("retailTradeCommodity") RetailTradeCommodity retailTradeCommodity);
}
