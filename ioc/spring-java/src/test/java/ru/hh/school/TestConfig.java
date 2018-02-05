package ru.hh.school;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.hh.school.connection.DataSource;
import ru.hh.school.connection.TestDataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class TestConfig {

  @Bean
  // returns interface
  public DataSource dataSource() {
    return new TestDataSource(false);
  }

  @Bean
  public DataSource roDataSource() {
    return new TestDataSource(true);
  }

}
