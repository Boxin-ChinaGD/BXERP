package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.ConfigGeneral;

@Repository
public interface ConfigGeneralMapper extends JpaRepository<ConfigGeneral, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_Name VARCHAR(60) NOT NULL," +
            "F_Value VARCHAR(128) NOT NULL" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO T_ConfigGeneral (F_ID,F_Name,F_Value)VALUES (" +
            ":#{#c.ID},:#{#c.name},:#{#c.value});", nativeQuery = true)
    void create(@Param("c") ConfigGeneral configGeneral);
}
