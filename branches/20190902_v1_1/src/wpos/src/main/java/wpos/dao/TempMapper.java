package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.Brand;

@Repository
public interface TempMapper extends JpaRepository<Brand, Integer> {

    @Transactional
    @Modifying
    @Query(value = "DROP TABLE temp;", nativeQuery = true)
    void dropTempTable();

    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE temp" +
            "(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_Name VARCHAR (20) NOT NULL," +
            "F_SyncDatetime DATETIME," +
            "F_SyncType VARCHAR(1)" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO temp SELECT F_ID, F_Name, F_SyncDatetime, F_SyncType FROM T_Brand;", nativeQuery = true)
    void create();

    @Transactional
    @Modifying
    @Query(value = "DROP TABLE T_Brand;", nativeQuery = true)
    void dropBrandTable();

    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_Brand" +
            "(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_Name VARCHAR (20) NOT NULL," +
            "F_SyncDatetime DATETIME," +
            "F_SyncType VARCHAR(1)" +
            ");", nativeQuery = true)
    void createBrandTable();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO T_Brand SELECT F_ID, F_Name, F_SyncDatetime, F_SyncType FROM temp;", nativeQuery = true)
    void insertBrand();
}
