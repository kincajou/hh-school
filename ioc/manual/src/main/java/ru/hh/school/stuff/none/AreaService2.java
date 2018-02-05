package ru.hh.school.stuff.none;

public class AreaService2 {

  private AreaDao areaDao;

  public boolean haveAreas() {
    if (areaDao == null) {
      areaDao = new AreaDao();
    }
    return areaDao.countAll() > 0;
  }
}
