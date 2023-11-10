package io.fluent.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;


@Entity(name = "authors")
@Data
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int age;
    private String name;
    private String genre;

}
