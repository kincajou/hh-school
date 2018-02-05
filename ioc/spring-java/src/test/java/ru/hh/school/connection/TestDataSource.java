package ru.hh.school.connection;

public class TestDataSource implements DataSource {

  private boolean connected;

  public TestDataSource(boolean connected) {
    this.connected = connected;
  }

  public boolean isConnected() {
    return connected;
  }

}
