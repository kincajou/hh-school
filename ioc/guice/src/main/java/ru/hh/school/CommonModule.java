package ru.hh.school;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import javax.inject.Named;

// this is CommonConfig in spring-java
public class CommonModule extends AbstractModule {

  @Override
  protected void configure() {
    // can bind this way - if there is default constructor or constructor with @Inject
    // same as <bean ... /> in spring
    bind(AreaDao.class).in(Singleton.class);
  }

  @Provides
  @Singleton
  // can bind this way, if you need to write some custom instantiation logic
  // same as @Bean annotated method
  public AreaService provideAreaService(AreaDao areaDao) {
    return new AreaService(areaDao);
  }

  @Provides
  @Named("millis")
  public Long provideMillis() {
    return System.currentTimeMillis();
  }

}
