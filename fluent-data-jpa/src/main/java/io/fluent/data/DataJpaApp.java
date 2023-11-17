package io.fluent.data;

import io.fluent.data.app.AppInitializer;
import io.fluent.data.jpa.repo.AuthorRepository;
import io.fluent.data.jpa.service.BookstoreService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class DataJpaApp  {
    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }
    public static void main(String[] args) {
        SpringApplication.run(DataJpaApp.class,args);
    }
}
