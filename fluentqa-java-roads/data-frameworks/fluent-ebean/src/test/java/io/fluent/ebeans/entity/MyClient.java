package io.fluent.ebeans.entity;

import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;
import lombok.Data;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Data
public class MyClient extends BaseDomain {

    @NotNull
    @Length(100)
    String name;

    LocalDate registered;

    public MyClient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDate registered) {
        this.registered = registered;
    }
}
