package be.tvw.customer;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan
@PropertySource("classpath:app-persistence.properties")
@EntityScan({"be.tvw.customer.entity"})
@EnableJpaRepositories(considerNestedRepositories = true)
@Import({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class DataConfig {

}
