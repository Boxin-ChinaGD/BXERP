package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.Commodity;
import wpos.model.CommodityShopInfo;

import java.util.List;

@Repository
public interface CommodityShopInfoMapper extends JpaRepository<CommodityShopInfo, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_CommodityID INT NOT NULL," +
            "F_ShopID INT NOT NULL," +
            "F_LatestPricePurchase Decimal(20,6) NOT NULL," +
            "F_PriceRetail Decimal(20,6) NOT NULL," +
            "F_NO INT NOT NULL," +
            "F_NOStart INT NOT NULL DEFAULT -1," +
            "F_PurchasingPriceStart Decimal(20,6)," +
            "F_CurrentWarehousingID INT NULL" +
            ");"
            , nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO T_CommodityShopInfo (F_ID,F_CommodityID,F_ShopID,F_LatestPricePurchase,F_PriceRetail,F_NO,F_NOStart,F_PurchasingPriceStart," +
            "F_CurrentWarehousingID) VALUES (" +
            ":#{#c.ID},:#{#c.commodityID},:#{#c.shopID},:#{#c.latestPricePurchase},:#{#c.priceRetail},:#{#c.NO},:#{#c.nOStart},:#{#c.purchasingPriceStart}," +
            ":#{#c.currentWarehousingID}" +
            ");", nativeQuery = true)
    void create(@Param("c") CommodityShopInfo commodityShopInfo);

//    @Transactional
//    @Modifying
//    @Query(value = "delete from T_CommodityShopInfo where F_ID >= ?1 and F_ID <= ?2", nativeQuery = true)
//    void deleteByRangeID(Integer start, Integer end);
//
//    @Transactional
//    @Modifying
//    @Query(value = "delete from T_CommodityShopInfo where F_ID > ?1", nativeQuery = true)
//    void deleteBiggerIDs(Integer id);
//
//    @Transactional
//    @Modifying
//    @Query(value = "delete from T_CommodityShopInfo where F_ID < ?1", nativeQuery = true)
//    void deleteSmallerIDs(Integer id);

    @Transactional
    @Modifying
    @Query(value = "delete from T_CommodityShopInfo where F_CommodityID = ?1", nativeQuery = true)
    void deleteByCommodityID(Integer id);

//    @Transactional
//    @Modifying
//    @Query(value = "select F_ID,F_CommodityID,F_ShopID,F_LatestPricePurchase,F_PriceRetail,F_NO,F_NOStart,F_PurchasingPriceStart,F_CurrentWarehousingID from T_CommodityShopInfo where F_CommodityID = ?1 and F_ShopID = ?2", nativeQuery = true)
//    List<CommodityShopInfo> findByCommoditIdAndShopID(Integer commodityID, Integer shopID);
}
