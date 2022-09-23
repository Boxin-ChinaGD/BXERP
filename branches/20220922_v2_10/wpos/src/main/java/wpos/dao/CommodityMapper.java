package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.Commodity;

@Repository
public interface CommodityMapper extends JpaRepository<Commodity, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_Status INT NOT NULL DEFAULT 0," +
            "F_Name VARCHAR (32) NOT NULL," +
            "F_ShortName VARCHAR (32)," +
            "F_Specification VARCHAR (8) NOT NULL," +
            "F_PackageUnitID INT," +
            "F_PurchasingUnit VARCHAR (16) NULL," +
            "F_BrandID INT," +
            "F_CategoryID INT," +
            "F_MnemonicCode VARCHAR (32) NOT NULL," +
            "F_PricingType INT NOT NULL," +
            "F_LatestPricePurchase Decimal(20,6) NOT NULL," +
            "F_PriceRetail Decimal(20,6) NOT NULL," +
            "F_PriceVIP Decimal(20,6) NOT NULL," +
            "F_PriceWholesale Decimal(20,6) NOT NULL," +
            "F_CanChangePrice INT NOT NULL," +
            "F_RuleOfPoint INT NULL," +
            "F_Picture VARCHAR (128) NULL," +
            "F_ShelfLife INT NULL," +
            "F_ReturnDays INT NOT NULL," +
            "F_CreateDate DATETIME," +
            "F_PurchaseFlag INT NULL," +
            "F_RefCommodityID INT NOT NULL DEFAULT 0," +
            "F_RefCommodityMultiple INT NOT NULL DEFAULT 0," +
            "F_Tag VARCHAR (32) NOT NULL," +
            "F_NO INT NOT NULL," +
            "F_Type INT NOT NULL," +
            "F_NOStart INT NOT NULL DEFAULT -1," +
            "F_PurchasingPriceStart Decimal(20,6)," +
            "F_StartValueRemark Varchar(50) NULL," +
            "F_CreateDatetime DATETIME," +
            "F_UpdateDatetime DATETIME," +
            "F_PropertyValue1 VARCHAR(50) NULL," +
            "F_PropertyValue2 VARCHAR(50) NULL," +
            "F_PropertyValue3 VARCHAR(50) NULL," +
            "F_PropertyValue4 VARCHAR(50) NULL," +
            "F_CurrentWarehousingID INT NULL," +
            "F_SyncType VARCHAR(1)," +
            "F_SyncDatetime DATETIME" +
            ");"
            , nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO T_Commodity (F_ID,F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID," +
            "F_MnemonicCode,F_PricingType,F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture," +
            "F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_NO,F_Type,F_NOStart,F_PurchasingPriceStart," +
            "F_StartValueRemark,F_CreateDatetime,F_UpdateDatetime,F_PropertyValue1,F_PropertyValue2,F_PropertyValue3,F_PropertyValue4,F_CurrentWarehousingID," +
            "F_SyncType,F_SyncDatetime) VALUES (" +
            ":#{#c.ID},:#{#c.status},:#{#c.name},:#{#c.shortName},:#{#c.specification},:#{#c.packageUnitID},:#{#c.purchasingUnit},:#{#c.brandID},:#{#c.categoryID}," +
            ":#{#c.mnemonicCode},:#{#c.pricingType},:#{#c.latestPricePurchase},:#{#c.priceRetail},:#{#c.priceVIP},:#{#c.priceWholesale},:#{#c.canChangePrice},:#{#c.ruleOfPoint},:#{#c.picture}," +
            ":#{#c.shelfLife},:#{#c.returnDays},:#{#c.createDate},:#{#c.purchaseFlag},:#{#c.refCommodityID},:#{#c.refCommodityMultiple},:#{#c.tag},:#{#c.NO},:#{#c.type},:#{#c.nOStart},:#{#c.purchasingPriceStart}," +
            ":#{#c.startValueRemark},:#{#c.createDatetime},:#{#c.updateDatetime},:#{#c.propertyValue1},:#{#c.propertyValue2},:#{#c.propertyValue3},:#{#c.propertyValue4},:#{#c.currentWarehousingID}," +
            ":#{#c.syncType},:#{#c.syncDatetime}" +
            ");", nativeQuery = true)
    void create(@Param("c") Commodity commodity);

    @Transactional
    @Modifying
    @Query(value = "delete from T_Commodity where F_ID >= ?1 and F_ID <= ?2", nativeQuery = true)
    void deleteByRangeID(Integer start, Integer end);

    @Transactional
    @Modifying
    @Query(value = "delete from T_Commodity where F_ID > ?1", nativeQuery = true)
    void deleteBiggerIDs(Integer id);

    @Transactional
    @Modifying
    @Query(value = "delete from T_Commodity where F_ID < ?1", nativeQuery = true)
    void deleteSmallerIDs(Integer id);
}
