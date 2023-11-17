package io.fluent.data.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class BaseEntity<U> {        

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @GeneratedValue(strategy=GenerationType.AUTO, generator="native")
    @GenericGenerator(name="native", strategy="native")
    protected Long id;
    
    @CreatedDate
    protected LocalDateTime created;
 
    @CreatedBy
    protected U createdBy;
    
    @LastModifiedDate
    protected LocalDateTime lastModified;          
 
    @LastModifiedBy
    protected U lastModifiedBy;
}