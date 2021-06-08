package com.kon.EShop.repository;

import com.kon.EShop.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface UnitRepository extends JpaRepository<Unit, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Unit u WHERE u.id=:id")
    int delete(@Param("id") Long id);
}
