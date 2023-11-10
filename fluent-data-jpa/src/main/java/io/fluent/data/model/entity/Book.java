package io.fluent.data.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.io.Serializable;


@Entity(name = "books")
@Data
public class Book extends BaseEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String isbn;
    private double price;

    @Formula("price - price * 0.25")
    private double discounted;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;


    @Generated(value = GenerationTime.ALWAYS)
    @Column(insertable = false, updatable = false /*, columnDefinition = "double AS (price - price * 0.25)"*/)
    private double discountedAgain;
}
