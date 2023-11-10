package io.fluent.data;

import io.fluent.data.app.AppInitializer;
import io.fluent.data.jpa.repo.AuthorRepository;
import io.fluent.data.jpa.service.BookstoreService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class DataJpaApp  {

    public static void main(String[] args) {
        SpringApplication.run(DataJpaApp.class,args);
    }
}
