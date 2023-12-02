package by.bsuir.springmvcproject.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan
@EnableJpaRepositories(basePackages = "by.bsuir.springmvcproject.repositories")
public class ComponentScanConfig {
}
