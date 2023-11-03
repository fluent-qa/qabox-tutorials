package io.fluent.ebeans.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Entity;

@Data
@Entity
public class Customer {

  @Id
  private long id;

  private String name;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
  