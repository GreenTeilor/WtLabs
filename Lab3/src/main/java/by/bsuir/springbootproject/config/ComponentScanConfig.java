package by.bsuir.springbootproject.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan
@EnableJpaRepositories(basePackages = "by.bsuir.springbootproject.repositories")
public class ComponentScanConfig {
}
