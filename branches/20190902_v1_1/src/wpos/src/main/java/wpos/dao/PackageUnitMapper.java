package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.PackageUnit;

@Repository
public interface PackageUnitMapper extends JpaRepository<PackageUnit, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_Name VARCHAR (8) NOT NULL," +
            "F_CreateDatetime DATETIME," +
            "F_UpdateDatetime DATETIME" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO T_PackageUnit (F_ID,F_Name,F_CreateDatetime,F_UpdateDatetime) VALUES (" +
            ":#{#p.ID},:#{#p.name},:#{#p.createDatetime},:#{#p.updateDatetime});", nativeQuery = true)
    void create(@Param("p") PackageUnit packageUnit);
}
