package io.fluent.data.model.dto;

import io.fluent.data.model.entity.Author;
import lombok.Data;


@Data
public class BookstoreDto {
    private  Author author;
    private  String title;
}
