package ru.hh.school;

import ru.hh.school.connection.DataSource;

public class AreaDao {

  private DataSource dataSource;

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
