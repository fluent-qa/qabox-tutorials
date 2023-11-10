package io.fluent.data.jpa.repo;

import io.fluent.data.model.dto.AuthorDto;
import io.fluent.data.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepo extends JpaRepository<Author,Long> {

    @Query(value = "SELECT ROW_NUMBER() OVER(ORDER BY age) "
            + "rowNum, name, age FROM author",
            nativeQuery = true)
    List<AuthorDto> fetchWithSeqNumber1();

    @Query(value = "SELECT ROW_NUMBER() OVER() "
            + "rowNum, name, age FROM author ORDER BY age",
            nativeQuery = true)
    List<AuthorDto> fetchWithSeqNumber2();

    @Query(value = "SELECT ROW_NUMBER() OVER(ORDER BY age) "
            + "rowNum, name, age FROM author ORDER BY name",
            nativeQuery = true)
    List<AuthorDto> fetchWithSeqNumber3();

    @Query(value = "SELECT ROW_NUMBER() OVER(ORDER BY age, name DESC) "
            + "rowNum, name, age FROM author",
            nativeQuery = true)
    List<AuthorDto> fetchWithSeqNumber4();
}
