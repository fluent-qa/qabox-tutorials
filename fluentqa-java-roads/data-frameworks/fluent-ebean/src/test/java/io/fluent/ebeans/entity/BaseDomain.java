package io.fluent.ebeans.entity;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.Instant;

@Data
@MappedSuperclass
public abstract class BaseDomain extends Model {

  @Id
  long id;

  @Version
  Long version;

  @WhenCreated
  Instant whenCreated;

  @WhenModified
  Instant whenModified;

}