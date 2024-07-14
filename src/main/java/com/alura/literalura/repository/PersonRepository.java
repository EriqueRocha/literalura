package com.alura.literalura.repository;

import com.alura.literalura.model.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {

    boolean existsByName(String name);

    PersonEntity findByName (String name);

    @Query("SELECT p FROM PersonEntity p WHERE :year BETWEEN p.birth_year AND p.death_year")
    List<PersonEntity> findByYearInRange(@Param("year") Integer year);

}
