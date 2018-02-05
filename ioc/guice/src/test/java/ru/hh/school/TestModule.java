package ru.hh.school;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import javax.inject.Named;
import ru.hh.school.connection.DataSource;
import ru.hh.school.connection.TestDataSource;

public class TestModule extends AbstractModule {
  @Override
  protected void configure() {

  }

  @Provides
  @Singleton
  public DataSource provideMasterDataSource() {
    return new TestDataSource(false);
  }

  @Provides
  @Singleton
  @Named("roDataSource")
  public DataSource provideRoDataSource() {
    return new TestDataSource(true);
  }

}
