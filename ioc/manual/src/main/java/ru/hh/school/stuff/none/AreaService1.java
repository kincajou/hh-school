package ru.hh.school.stuff.none;

public class AreaService1 {

  private final AreaDao areaDao;

  public AreaService1() {
    this.areaDao = new AreaDao();
  }

  public boolean haveAreas() {
    return areaDao.countAll() > 0;
  }
}
