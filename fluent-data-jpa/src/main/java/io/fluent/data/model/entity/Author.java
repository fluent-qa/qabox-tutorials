package io.fluent.data.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


@Entity(name = "authors")
@Data
public class Author extends BaseEntity<String>  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "BINARY(16)")
    private UUID uuid;

//    @Id
//    // This will disable insert batching - AVOID IT!
//    // @GeneratedValue(strategy = GenerationType.IDENTITY)
//
//    // This will work, but better use the below solution to reduce database roundtrips
//    // @GeneratedValue(strategy = GenerationType.AUTO)
//
//    // This will allow insert batching and optimizes the identifiers
//    // generation via the hi/lo algorithm which generated in-memory identifiers
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hilo")
//    @GenericGenerator(name = "hilo",
//            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
//            parameters = {
//                    @Parameter(name = "sequence_name", value = "hilo_sequence"),
//                    @Parameter(name = "initial_value", value = "1"),
//                    @Parameter(name = "increment_size", value = "10"),
//                    @Parameter(name = "optimizer", value = "hilo")
//            })
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int age;
    private String name;
    private String genre;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] avatar;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "author", orphanRemoval = true)
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        this.books.add(book);
        book.setAuthor(this);
    }

    public void removeBook(Book book) {
        book.setAuthor(null);
        this.books.remove(book);
    }

    public void removeBooks() {
        Iterator<Book> iterator = this.books.iterator();

        while (iterator.hasNext()) {
            Book book = iterator.next();

            book.setAuthor(null);
            iterator.remove();
        }
    }
}
