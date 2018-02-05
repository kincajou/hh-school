package ru.hh.school.connection;

public class DbDataSource implements  DataSource {

  private String host;
  private String login;
  private String password;

  public DbDataSource(String host, String login, String password) {
    this.host = host;
    this.login = login;
    this.password = password;
  }

  public boolean isConnected() {
    System.out.println("Connected to: " + login + ":" + password + "@" + host);
    return true;
  }

}
