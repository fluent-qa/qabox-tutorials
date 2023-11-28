package io.fluent.data.jpa.repo;

import io.fluent.data.model.entity.Author;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest()
//@TestPropertySource(properties = {
//        "spring.jpa.hibernate.ddl-auto=validate"
//})
class AuthorRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(authorRepository).isNotNull();
    }

    private Author setupAuthorData() throws IOException {
        Author author = new Author();
        author.setAge(10);
        author.setName("name");
        author.setGenre("genre");
        authorRepository.save(author);
        Author mt = new Author();
        mt.setId(1L);
        mt.setName("Martin Ticher");
        mt.setAge(43);
        mt.setGenre("Horror");
        mt.setAvatar(Files.readAllBytes(new File("avatars/mt.png").toPath()));

        Author cd = new Author();
        cd.setId(2L);
        cd.setName("Carla Donnoti");
        cd.setAge(31);
        cd.setGenre("Science Fiction");
        cd.setAvatar(Files.readAllBytes(new File("avatars/cd.png").toPath()));

        Author re = new Author();
        re.setId(3L);
        re.setName("Rennata Elibol");
        re.setAge(46);
        re.setGenre("Fantasy");
        re.setAvatar(Files.readAllBytes(new File("avatars/re.png").toPath()));

        authorRepository.save(mt);
        authorRepository.save(cd);
        authorRepository.save(re);
        return author;
    }

    @Test
    void testSaveEntity() throws IOException {
        System.out.println(setupAuthorData().getId());
    }

    @Test
    @Order(2)
        //data jpa expressions
    void testFindQuery() throws IOException {
        setupAuthorData();
        List<Author> authors = authorRepository.findByAgeGreaterThanEqual(5);
        assertThat(authors.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("save or merge")
    public void testFetchAuthor() throws IOException {
        setupAuthorData();
        Author author = authorRepository.findById(3L).orElseThrow();

        if (author.getAge() < 40) {
            author.getAvatar();
        } else {
            author.setAvatar(null);
        }

        authorRepository.save(author);
    }

    @Test
        //data jpa expressions
    void testFindNativeQueryForDTO() throws IOException {

        List<Author> authors = authorRepository.findAuthorByNativeQuery(5);
        System.out.println(authors.get(0));
        assertThat(authors.size()).isGreaterThan(0);
    }
}