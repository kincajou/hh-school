package ru.hh.school;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import ru.hh.school.connection.DataSource;
import ru.hh.school.connection.DbDataSource;

// this is ProdConfig in spring-java
public class ProdModule extends AbstractModule {

  @Override
  protected void configure() {

    // have to manually load properties, guice is less sophisticated
    Properties properties = loadProperties();

    // guice don't have something like @Value in spring
    bind(String.class).annotatedWith(Names.named("db.host")).toInstance(properties.getProperty("db.host"));
    bind(String.class).annotatedWith(Names.named("db.login")).toInstance(properties.getProperty("db.login"));
    bind(String.class).annotatedWith(Names.named("db.password")).toInstance(properties.getProperty("db.password"));

    bind(String.class).annotatedWith(Names.named("db.ro.host")).toInstance(properties.getProperty("db.ro.host"));
    bind(String.class).annotatedWith(Names.named("db.ro.login")).toInstance(properties.getProperty("db.ro.login"));
    bind(String.class).annotatedWith(Names.named("db.ro.password")).toInstance(properties.getProperty("db.ro.password"));
  }

  private Properties loadProperties() {
    Properties properties = new Properties();
    try (InputStream stream = this.getClass().getResourceAsStream("/application.properties")) {
      properties.load(stream);
    }
    catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
    return properties;
  }

  @Provides
  @Singleton
  // unnamed - default
  static DataSource provideMasterDataSource(
    @Named("db.host") String host,
    @Named("db.login") String login,
    @Named("db.password") String password) {
    return new DbDataSource(host, login, password);
  }

  @Provides
  @Singleton
  // can be injected only by explicitly specifying the name
  @Named("roDataSource")
  static DataSource provideRoDataSource(
    @Named("db.ro.host") String host,
    @Named("db.ro.login") String login,
    @Named("db.ro.password") String password) {
    return new DbDataSource(host, login, password);
  }

}
