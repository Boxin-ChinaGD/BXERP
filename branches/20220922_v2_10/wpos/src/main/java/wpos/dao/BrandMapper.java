package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.Brand;

@Repository
public interface BrandMapper extends JpaRepository<Brand, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}" +
            "(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_Name VARCHAR (20) NOT NULL," +
            "F_SyncDatetime DATETIME," +
            "F_SyncType VARCHAR(1)" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO T_Brand (F_ID,F_Name,F_SyncDatetime,F_SyncType) VALUES (" +
            ":#{#b.ID}, :#{#b.name}, :#{#b.syncDatetime}, :#{#b.syncType}" +
            ");", nativeQuery = true)
    void create(@Param("b") Brand brand);
}
