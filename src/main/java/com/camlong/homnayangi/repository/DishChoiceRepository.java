package com.camlong.homnayangi.repository;

import com.camlong.homnayangi.dto.DishChoiceCount;
import com.camlong.homnayangi.entity.DishChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishChoiceRepository extends JpaRepository<DishChoice, Long> {

    @Query("""
            select new com.camlong.homnayangi.dto.DishChoiceCount(dc.dish.id, count(dc.id), max(dc.createdAt))
            from DishChoice dc
            where dc.user.id = :userId
            group by dc.dish.id
            """)
    List<DishChoiceCount> findChoiceCountByUserId(@Param("userId") Long userId);
}
