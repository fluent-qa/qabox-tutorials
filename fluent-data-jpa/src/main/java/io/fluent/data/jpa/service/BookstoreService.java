package io.fluent.data.jpa.service;

import java.util.List;
import java.util.Map;

import io.fluent.data.jpa.repo.AuthorRepository;
import io.fluent.data.jpa.repo.BookRepository;
import io.fluent.data.jpa.repo.BookReviewRepository;
import io.fluent.data.model.entity.Author;
import io.fluent.data.model.entity.Book;
import io.fluent.data.model.entity.BookReview;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

@Service
public class BookstoreService {

    @Resource
    private final AuthorRepository authorRepository;

    private final JdbcTemplate jdbcTemplate;
    private final BookReviewRepository bookReviewRepository;

    @Resource
    private final BookRepository bookRepository;
    public BookstoreService(AuthorRepository authorRepository, JdbcTemplate jdbcTemplate, BookReviewRepository bookReviewRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.bookReviewRepository = bookReviewRepository;
        this.bookRepository = bookRepository;
    }


    @Transactional
    public void registerAuthor() {

        Author a1 = new Author();
        a1.setName("Quartis Young");
        a1.setGenre("Anthology");
        a1.setAge(34);

        Author a2 = new Author();
        a2.setName("Mark Janel");
        a2.setGenre("Anthology");
        a2.setAge(23);

        Book b1 = new Book();
        b1.setIsbn("001");
        b1.setTitle("The Beatles Anthology");

        Book b2 = new Book();
        b2.setIsbn("002");
        b2.setTitle("A People's Anthology");

        Book b3 = new Book();
        b3.setIsbn("003");
        b3.setTitle("Anthology Myths");

        a1.addBook(b1);
        a1.addBook(b2);
        a2.addBook(b3);

        authorRepository.save(a1);
        authorRepository.save(a2);
    }

    @Transactional
    public void updateAuthor() {
        Author author = authorRepository.findByName("Mark Janel");

        author.setAge(45);
    }

    @Transactional
    public void updateBooks() {
        Author author = authorRepository.findByName("Quartis Young");
        List<Book> books = author.getBooks();

        for (Book book : books) {
            book.setIsbn("not available");
        }
    }

    public void insertBook() {
        Book book = new Book();

        book.setTitle("Ancient History");
        book.setIsbn("001-AH");
        book.setPrice(13.99);

        bookRepository.save(book);

        System.out.println("Discounted price after insert: " + book.getDiscounted());
    }

    public void fetchAnthologyAuthors() {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("FETCH_AUTHOR_BY_GENRE");
// .returningResultSet("AuthorResultSet",
//                BeanPropertyRowMapper.newInstance(AuthorDto.class));
        List<Author> authors = simpleJdbcCall.execute(Map.of("p_genre", "Anthology")).entrySet()
                .stream()
                .filter(m -> "#result-set-1".equals(m.getKey()))
                .map(m -> (List<Map<String, Object>>) m.getValue())
                .flatMap(List::stream)
                .map(BookstoreService::fetchAuthor)
                .collect(toList());

        System.out.println("Result: " + authors);
    }

    public static Author fetchAuthor(Map<String, Object> data) {

        Author author = new Author();

        author.setId((Long) data.get("id"));
        author.setName((String) data.get("name"));
        author.setGenre((String) data.get("genre"));
        author.setAge((int) data.get("age"));

        return author;
    }
    private final static String RESPONSE
            = "We check your review and get back to you with an e-mail ASAP :)";

    @Transactional
    public String postReview(BookReview bookReview) {

        Book book = bookRepository.getOne(1L);
        bookReview.setBook(book);

        bookReview.registerReviewEvent();

        bookReviewRepository.save(bookReview);

        return RESPONSE;
    }
}
