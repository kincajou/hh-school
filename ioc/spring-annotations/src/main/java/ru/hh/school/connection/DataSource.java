package ru.hh.school.connection;

// no @Component here means it will not be managed by scanning, need to 'register' it manually
public class DataSource {

  public boolean isConnected() {
    return true;
  }

}
