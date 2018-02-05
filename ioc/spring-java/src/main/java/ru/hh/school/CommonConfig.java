package ru.hh.school;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hh.school.connection.DataSource;

@Configuration
// @Configuration classes are same as @Component, etc. - @Inject works, they can be scanned, can have a constructor without annotations
public class CommonConfig {

  private DataSource masterDataSource;
  private DataSource roDataSource;

  // since there are two DataSource instances, they need to be qualified
  public CommonConfig(@Qualifier("dataSource") DataSource masterDataSource, @Qualifier("roDataSource") DataSource roDataSource) {
    this.masterDataSource = masterDataSource;
    this.roDataSource = roDataSource;
  }

  @Bean
  public AreaDao areaDao() {
    return new AreaDao(masterDataSource, roDataSource);
  }

  @Bean
  public AreaService areaService() {
    return new AreaService(areaDao());
  }

}
