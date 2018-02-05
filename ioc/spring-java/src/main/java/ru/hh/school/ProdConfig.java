package ru.hh.school;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import ru.hh.school.connection.DataSource;
import ru.hh.school.connection.DbDataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class ProdConfig {

  @Value("${db.host}")
  private String host;

  @Value("${db.login}")
  private String login;

  @Value("${db.password}")
  private String password;

  @Value("${db.ro.host}")
  private String roHost;

  @Value("${db.ro.login}")
  private String roLogin;

  @Value("${db.ro.password}")
  private String roPassword;

  @Bean
  // returns interface
  public DataSource dataSource() {
    return new DbDataSource(host, login, password);
  }

  @Bean
  // by default bean id = method name, so you can inject it elsewhere if there are several same classess
  public DataSource roDataSource() {
    return new DbDataSource(roHost, roLogin, roPassword);
  }

  // required to resolve @Value("${...}")
  @Bean
  public static PropertySourcesPlaceholderConfigurer propertyConfig() {
    return new PropertySourcesPlaceholderConfigurer();
  }
}
