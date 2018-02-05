package ru.hh.school;

import javax.inject.Inject;
import javax.inject.Named;
import ru.hh.school.connection.DataSource;

public class AreaDao {

  private DataSource masterDataSource;
  private DataSource roDataSource;

  @Inject
  // two equal classes distinguished by @Named
  public AreaDao(DataSource masterDataSource, @Named("roDataSource") DataSource roDataSource) {
    this.masterDataSource = masterDataSource;
    this.roDataSource = roDataSource;
  }

  public int countAll() {
    if (!masterDataSource.isConnected()) {
      throw new IllegalStateException("Not connected");
    }
    return 100;
  }

  public int countAllOnReplica() {
    if (!roDataSource.isConnected()) {
      throw new IllegalStateException("Not connected");
    }
    return 0;
  }

}
