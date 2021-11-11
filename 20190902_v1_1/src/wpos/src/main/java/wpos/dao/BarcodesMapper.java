package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.Barcodes;

@Repository
public interface BarcodesMapper extends JpaRepository<Barcodes, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}" +
            " (" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_CommodityID INT NOT NULL," +
            "F_Barcode VARCHAR (64) NOT NULL," +
            "F_CreateDatetime DATETIME," +
            "F_UpdateDatetime DATETIME," +
            "F_SyncDatetime DATETIME," +
            "F_SyncType VARCHAR (4)" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "insert into T_Barcodes (F_ID,F_Barcode,F_CommodityID,F_CreateDatetime,F_UpdateDatetime,F_SyncDatetime,F_SyncType) values " +
            "( :#{#barcodes.ID}, :#{#barcodes.barcode}, :#{#barcodes.commodityID}, :#{#barcodes.createDatetime}, :#{#barcodes.updateDatetime}," +
            ":#{#barcodes.syncDatetime}, :#{#barcodes.syncType} );", nativeQuery = true)
    int create(@Param("barcodes") Barcodes barcodes);


    @Transactional
    @Modifying
    @Query(value = "delete from T_Barcodes where F_ID >= ?1 and F_ID <= ?2", nativeQuery = true)
    void deleteByRangeID(Integer start, Integer end);

    @Transactional
    @Modifying
    @Query(value = "delete from T_Barcodes where F_ID > ?1", nativeQuery = true)
    void deleteBiggerIDs(Integer id);

    @Transactional
    @Modifying
    @Query(value = "delete from T_Barcodes where F_ID < ?1", nativeQuery = true)
    void deleteSmallerIDs(Integer id);
}
