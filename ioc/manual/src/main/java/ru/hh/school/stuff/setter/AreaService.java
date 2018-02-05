package ru.hh.school.stuff.setter;

public class AreaService {

  private AreaDao areaDao;

  public void setAreaDao(AreaDao areaDao) {
    this.areaDao = areaDao;
  }

  public boolean haveAreas() {
    return areaDao.countAll() > 0;
  }
}
