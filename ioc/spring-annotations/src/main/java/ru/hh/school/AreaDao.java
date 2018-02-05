package ru.hh.school;

import javax.inject.Inject;
import org.springframework.stereotype.Repository;
import ru.hh.school.connection.DataSource;

@Repository
// can be @Component
public class AreaDao {

  // can be injected either via constructor or via setter (bad!)
  private DataSource dataSource;

  @Inject
  // can omit @Inject for obvious constructors, spring knows it should inject anyway
  public AreaDao(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public int countAll() {
    if (!dataSource.isConnected()) {
      throw new IllegalStateException("Not connected");
    }
    return 100;
  }

}
