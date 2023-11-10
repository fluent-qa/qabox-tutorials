package io.fluent.data.jpa.repo;

import io.fluent.data.model.dto.AuthorDto;
import io.fluent.data.model.dto.BookstoreDto;
import io.fluent.data.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author,Long> {

    @Transactional(readOnly = true)
    List<Author> findByAgeGreaterThanEqual(int age);
    public Author findByName(String name);

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM authors a WHERE a.age >= ?1",nativeQuery = true)
    List<Author> findAuthorByNativeQuery(int age);

    //TODO: FetchDTO

//    @Transactional
//    @Modifying(flushAutomatically = true, clearAutomatically = true)
//    @Query("DELETE FROM Author a WHERE a IN ?1")
//    public int deleteInBulk(List<Author> authors);

//    @Transactional
//    @Modifying(flushAutomatically = true, clearAutomatically = true)
//    @Query(value = "UPDATE Author a SET a.age = a.age + 1, a.version = a.version + 1")
//    public int updateInBulk();
//
//    @Transactional
//    @Modifying(flushAutomatically = true, clearAutomatically = true)
//    @Query(value = "UPDATE Author a SET a.age = a.age + 1, a.version = a.version + 1 WHERE a IN ?1")
//    public int updateInBulk(List<Author> authors);

//    @Query(value = "{CALL FETCH_AUTHOR_BY_GENRE (:p_genre)}", nativeQuery = true)
//    List<Author> fetchByGenre(@Param("p_genre") String genre);
}
