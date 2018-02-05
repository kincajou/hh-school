package ru.hh.school.stuff.factory;

public class AreaDaoFactory {

  private static AreaDao INSTANCE;

  public AreaDaoFactory() {
    INSTANCE = new AreaDao();
  }

  public static AreaDao getInstance() {
    return INSTANCE;
  }

}
