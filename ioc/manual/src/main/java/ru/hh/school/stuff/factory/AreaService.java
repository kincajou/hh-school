package ru.hh.school.stuff.factory;

public class AreaService {

  public boolean haveAreas() {
    return AreaDaoFactory.getInstance().countAll() > 0;
  }
}
