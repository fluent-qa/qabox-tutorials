package io.fluent.data.app;

import io.fluent.data.domains.users.model.entity.User;
import io.fluent.data.jpa.auditor.AuditorAwareImpl;
import io.fluent.data.jpa.repo.AuthorRepository;
import io.fluent.data.jpa.service.BookstoreService;
import io.fluent.data.model.entity.Author;
import io.fluent.data.service.GitHubLookupService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;

@Configuration
@AllArgsConstructor
@Slf4j
public class AppInitializer implements ApplicationRunner {
    private final BookstoreService bookstoreService;
    private final AuthorRepository authorRepository;

    @Bean
    public ApplicationRunner init() {
        return args -> {
            System.out.println("start Application Initializer");
            run(args);
        };
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }

    @Autowired
    private GitHubLookupService gitHubLookupService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        authorRepository.deleteAll();
        System.out.println("start init test data");
        Author author = new Author();
        author.setAge(10);
        author.setName("name");
        author.setGenre("genre");
        authorRepository.save(author);
        Author mt = new Author();
        mt.setId(1L);
        mt.setName("Martin Ticher");
        mt.setAge(43);
        mt.setGenre("Horror");
        mt.setAvatar(Files.readAllBytes(new File("avatars/mt.png").toPath()));

        Author cd = new Author();
        cd.setId(2L);
        cd.setName("Carla Donnoti");
        cd.setAge(31);
        cd.setGenre("Science Fiction");
        cd.setAvatar(Files.readAllBytes(new File("avatars/cd.png").toPath()));

        Author re = new Author();
        re.setId(3L);
        re.setName("Rennata Elibol");
        re.setAge(46);
        re.setGenre("Fantasy");
        re.setAvatar(Files.readAllBytes(new File("avatars/re.png").toPath()));

        authorRepository.save(mt);
        authorRepository.save(cd);
        authorRepository.save(re);

        // Start the clock
        long start = System.currentTimeMillis();

        // Kick of multiple, asynchronous lookups
        CompletableFuture<User> page1 = gitHubLookupService.findUser("PivotalSoftware");
        CompletableFuture<User> page2 = gitHubLookupService.findUser("CloudFoundry");
        CompletableFuture<User> page3 = gitHubLookupService.findUser("Spring-Projects");
        CompletableFuture<User> page4 = gitHubLookupService.findUser("RameshMF");
        // Wait until they are all done
        CompletableFuture.allOf(page1, page2, page3, page4).join();

        // Print results, including elapsed time
        log.info("Elapsed time: " + (System.currentTimeMillis() - start));
        log.info("--> " + page1.get());
        log.info("--> " + page2.get());
        log.info("--> " + page3.get());
        log.info("--> " + page4.get());
    }
}
