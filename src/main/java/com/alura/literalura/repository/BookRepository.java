package com.alura.literalura.repository;

import com.alura.literalura.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    @Query("SELECT b FROM BookEntity b JOIN b.languages l WHERE l IN :languages")
    List<BookEntity> findByLanguagesIn(@Param("languages") List<String> languages);

    boolean existsByTitle (String titile);

    @Query("SELECT b FROM BookEntity b ORDER BY b.download_count DESC")
    List<BookEntity> findTopByDownloadCount();

}
