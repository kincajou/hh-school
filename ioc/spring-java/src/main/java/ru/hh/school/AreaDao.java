package ru.hh.school;

import ru.hh.school.connection.DataSource;

// no annotations here - everything is managed via Config class
public class AreaDao {

  private DataSource masterDataSource;
  private DataSource roDataSource;

  public AreaDao(DataSource masterDataSource, DataSource roDataSource) {
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
